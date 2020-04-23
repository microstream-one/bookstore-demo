package one.microstream.readmecorp.data;

import static one.microstream.X.coalesce;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

public interface Inventory extends Entity
{
	public Stream<Book> books();
		
	public int amount(final Book book);
	
	public Stream<Entry<Book, Integer>> slots();
	
	public int slotCount();
	
	
	public static class Default implements Inventory
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
		public Stream<Book> books()
		{
			return this.inventoryMap.keySet().stream();
		}
		
		@Override
		public int amount(final Book book)
		{
			return coalesce(
				this.inventoryMap.get(book),
				0
			);
		}
		
		@Override
		public Stream<Entry<Book, Integer>> slots()
		{
			return this.inventoryMap.entrySet().stream();
		}
		
		@Override
		public int slotCount()
		{
			return this.inventoryMap.size();
		}

	}
	
}
