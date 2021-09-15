package one.microstream.demo.bookstore.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.util.concurrent.ReadWriteLocked;
import one.microstream.persistence.types.Persister;
import one.microstream.reference.Lazy;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;

/**
 * All retail shops operated by this company.
 * <p>
 * This type is used to read and write the {@link Shop}s, their {@link Employee}s and {@link Inventory}s.
 * <p>
 * All operations on this type are thread safe.
 *
 * @see Data#shops()
 * @see ReadWriteLocked
 */
public interface Shops
{
	/**
	 * Adds a new shop and stores it with the {@link BookStoreDemo}'s {@link EmbeddedStorageManager}.
	 * <p>
	 * This is a synonym for:<pre>this.add(shop, BookStoreDemo.getInstance().storageManager())</pre>
	 *
	 * @param shop the new shop
	 */
	public default void add(final Shop shop)
	{
		this.add(shop, BookStoreDemo.getInstance().storageManager());
	}

	/**
	 * Adds a new shop and stores it with the given persister.
	 *
	 * @param shop the new shop
	 * @param persister the persister to store it with
	 * @see #add(Shop)
	 */
	public void add(Shop shop, Persister persister);

	/**
	 * Adds a range of new shops and stores it with the {@link BookStoreDemo}'s {@link EmbeddedStorageManager}.
	 * <p>
	 * This is a synonym for:<pre>this.addAll(shops, BookStoreDemo.getInstance().storageManager())</pre>
	 *
	 * @param shops the new shops
	 */
	public default void addAll(final Collection<? extends Shop> shops)
	{
		this.addAll(shops, BookStoreDemo.getInstance().storageManager());
	}

	/**
	 * Adds a range of new shops and stores it with the given persister.
	 *
	 * @param shops the new shops
	 * @param persister the persister to store them with
	 * @see #addAll(Collection)
	 */
	public void addAll(Collection<? extends Shop> shops, Persister persister);

	/**
	 * Gets the total amount of all shops.
	 *
	 * @return the amount of shops
	 */
	public int shopCount();

	/**
	 * Gets all shops as a {@link List}.
	 * Modifications to the returned list are not reflected to the backed data.
	 *
	 * @return all shops
	 */
	public List<Shop> all();

	/**
	 * Clears all {@link Lazy} references used by all shops.
	 * This frees the used memory but you do not lose the persisted data. It is loaded again on demand.
	 *
	 * @see Shop#clear()
	 */
	public void clear();

	/**
	 * Executes a function with a {@link Stream} of {@link Shop}s and returns the computed value.
	 *
	 * @param <T> the return type
	 * @param streamFunction computing function
	 * @return the computed result
	 */
	public <T> T compute(Function<Stream<Shop>, T> streamFunction);

	/**
	 * Executes a function with a {@link Stream} of {@link InventoryItem}s and returns the computed value.
	 *
	 * @param <T> the return type
	 * @param streamFunction computing function
	 * @return the computed result
	 */
	public <T> T computeInventory(Function<Stream<InventoryItem>, T> function);

	/**
	 * Gets the shop with a specific name or <code>null</code> if none was found.
	 *
	 * @param name the name to search by
	 * @return the matching shop or <code>null</code>
	 */
	public Shop ofName(String name);


	/**
	 * Default implementation of the {@link Shops} interface.
	 * <p>
	 * It utilizes a {@link ReadWriteLocked.Scope} to ensure thread safe reads and writes.
	 */
	public static class Default extends ReadWriteLocked.Scope implements Shops
	{
		/**
		 * Simple list to hold the shops.
		 */
		private final List<Shop> shops = new ArrayList<>(1024);

		Default()
		{
			super();
		}

		@Override
		public void add(
			final Shop shop,
			final Persister persister
		)
		{
			this.write(() -> {
				this.shops.add(shop);
				persister.store(this.shops);
			});
		}

		@Override
		public void addAll(
			final Collection<? extends Shop> shops,
			final Persister persister
		)
		{
			this.write(() -> {
				this.shops.addAll(shops);
				persister.store(this.shops);
			});
		}

		@Override
		public int shopCount()
		{
			return this.read(
				this.shops::size
			);
		}

		@Override
		public List<Shop> all()
		{
			return this.read(() ->
				new ArrayList<>(this.shops)
			);
		}

		@Override
		public void clear()
		{
			this.write(() ->
				this.shops.forEach(Shop::clear)
			);
		}

		@Override
		public <T> T compute(
			final Function<Stream<Shop>, T> streamFunction
		)
		{
			return this.read(() ->
				streamFunction.apply(
					this.shops.parallelStream()
				)
			);
		}

		@Override
		public <T> T computeInventory(
			final Function<Stream<InventoryItem>, T> function
		)
		{
			return this.read(() ->
				function.apply(
					this.shops.parallelStream().flatMap(shop ->
						shop.inventory().compute(entries ->
							entries.map(entry -> InventoryItem.New(shop, entry.getKey(), entry.getValue()))
						)
					)
				)
			);
		}

		@Override
		public Shop ofName(final String name)
		{
			return this.read(() ->
				this.shops.stream()
					.filter(shop -> shop.name().equals(name))
					.findAny()
					.orElse(null)
			);
		}

	}

}
