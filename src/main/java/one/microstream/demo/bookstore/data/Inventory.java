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
public class Inventory extends ReadWriteLocked
{
	private final Map<Book, Integer> inventoryMap;

	public Inventory()
	{
		this(new HashMap<>());
	}

	/**
	 * Package-private constructor used by {@link RandomDataGenerator}.
	 */
	Inventory(final Map<Book, Integer> inventoryMap)
	{
		super();
		
		this.inventoryMap = inventoryMap;
	}
	
	/**
	 * Get the amount of a specific book in this inventory.
	 *
	 * @param book the book
	 * @return the amount of the given book in this inventory or 0
	 */
	public int amount(final Book book)
	{
		return this.read(() -> coalesce(
			this.inventoryMap.get(book),
			0
		));
	}

	/**
	 * Executes a function with a {@link Stream} of {@link Entry}s and returns the computed value.
	 *
	 * @param <T> the return type
	 * @param streamFunction computing function
	 * @return the computed result
	 */
	public <T> T compute(final Function<Stream<Entry<Book, Integer>>, T> streamFunction)
	{
		return this.read(() ->
			streamFunction.apply(
				this.inventoryMap.entrySet().stream()
			)
		);
	}

	/**
	 * Get the total amount of slots (different books) in this inventory.
	 *
	 * @return the amount of slots
	 */
	public int slotCount()
	{
		return this.read(() ->
			this.inventoryMap.size()
		);
	}

	/**
	 * Gets all books and their amount as a {@link List}.
	 * Modifications to the returned list are not reflected to the backed data.
	 *
	 * @return all books and their amount
	 */
	public List<Entry<Book, Integer>> slots()
	{
		return this.read(() ->
			new ArrayList<>(this.inventoryMap.entrySet())
		);
	}

	/**
	 * Gets all books as a {@link List}.
	 * Modifications to the returned list are not reflected to the backed data.
	 *
	 * @return all books
	 */
	public List<Book> books()
	{
		return this.read(() ->
			this.inventoryMap.keySet().stream().collect(toList())
		);
	}

}
