package one.microstream.demo.bookstore.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.util.concurrent.ReadWriteLocked;
import one.microstream.storage.types.StorageConnection;

public interface Shops
{
	public default void add(final Shop shop)
	{
		this.add(shop, BookStoreDemo.getInstance().storageManager());
	}

	public void add(Shop shop, StorageConnection storage);

	public default void addAll(final Collection<? extends Shop> shops)
	{
		this.addAll(shops, BookStoreDemo.getInstance().storageManager());
	}

	public void addAll(Collection<? extends Shop> shops, StorageConnection storage);

	public int shopCount();

	public List<Shop> all();

	public void clear();

	public <T> T compute(Function<Stream<Shop>, T> streamFunction);

	public Shop ofName(String name);


	public static class Default extends ReadWriteLocked.Scope implements Shops
	{
		private final List<Shop> shops = new ArrayList<>(1024);

		Default()
		{
			super();
		}

		@Override
		public void add(
			final Shop shop,
			final StorageConnection storage
		)
		{
			this.write(() -> {
				this.shops.add(shop);
				storage.store(this.shops);
			});
		}

		@Override
		public void addAll(
			final Collection<? extends Shop> shops,
			final StorageConnection storage
		)
		{
			this.write(() -> {
				this.shops.addAll(shops);
				storage.store(this.shops);
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
