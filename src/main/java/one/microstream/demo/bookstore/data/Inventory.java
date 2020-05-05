package one.microstream.demo.bookstore.data;

import static java.util.stream.Collectors.toList;
import static one.microstream.X.coalesce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Stream;

import one.microstream.demo.bookstore.util.concurrent.ReadWriteLocked;

/**
 * Inventory entity which holds {@link Book}s and amounts of them.
 * <p>
 * All operations on this type are thread safe.
 *
 * @see ReadWriteLocked
 */
public interface Inventory
{
	/**
	 * Get the amount of a specific book in this inventory.
	 *
	 * @param book the book
	 * @return the amount of the given book in this inventory or 0
	 */
	public int amount(final Book book);

	/**
	 * Executes a function with a {@link Stream} of {@link Entry}s and returns the computed value.
	 *
	 * @param <T> the return type
	 * @param streamFunction computing function
	 * @return the computed result
	 */
	public <T> T compute(Function<Stream<Entry<Book, Integer>>, T> streamFunction);

	/**
	 * Get the total amount of slots (different books) in this inventory.
	 *
	 * @return the amount of slots
	 */
	public int slotCount();

	/**
	 * Gets all books and their amount as a {@link List}.
	 * Modifications to the returned list are not reflected to the backed data.
	 *
	 * @return all books and their amount
	 */
	public List<Entry<Book, Integer>> slots();

	/**
	 * Gets all books as a {@link List}.
	 * Modifications to the returned list are not reflected to the backed data.
	 *
	 * @return all books
	 */
	public List<Book> books();


	/**
	 * Default implementation of the {@link Inventory} interface.
	 * <p>
	 * It utilizes a {@link ReadWriteLocked.Scope} to ensure thread safe reads and writes.
	 *
	 */
	public static class Default extends ReadWriteLocked.Scope implements Inventory
	{
		private final Map<Book, Integer> inventoryMap;

		Default()
		{
			this(new HashMap<>());
		}

		Default(
			final Map<Book, Integer> inventoryMap
		)
		{
			super();
			this.inventoryMap = inventoryMap;
		}

		@Override
		public int amount(final Book book)
		{
			return this.read(() -> coalesce(
				this.inventoryMap.get(book),
				0
			));
		}

		@Override
		public <T> T compute(
			final Function<Stream<Entry<Book, Integer>>, T> streamFunction
		)
		{
			return this.read(() ->
				streamFunction.apply(
					this.inventoryMap.entrySet().stream()
				)
			);
		}

		@Override
		public int slotCount()
		{
			return this.read(() ->
				this.inventoryMap.size()
			);
		}

		@Override
		public List<Entry<Book, Integer>> slots()
		{
			return this.read(() ->
				new ArrayList<>(this.inventoryMap.entrySet())
			);
		}

		@Override
		public List<Book> books()
		{
			return this.read(() ->
				this.inventoryMap.keySet().stream().collect(toList())
			);
		}

	}

}
