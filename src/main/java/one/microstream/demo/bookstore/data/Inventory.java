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

public interface Inventory
{
	public int amount(final Book book);

	public <T> T compute(Function<Stream<Entry<Book, Integer>>, T> streamFunction);

	public int slotCount();

	public List<Entry<Book, Integer>> slots();

	public List<Book> books();


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
