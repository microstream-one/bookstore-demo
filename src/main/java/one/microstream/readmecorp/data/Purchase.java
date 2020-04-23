
package one.microstream.readmecorp.data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public interface Purchase extends Entity
{
	public static interface Item extends Entity
	{
		public Book book();
		
		public int amount();
		
		public double itemTotal();
		
		public static Item New(
			final Book book,
			final int amount
		)
		{
			return new Default(book, amount);
		}
		
		public static class Default implements Item
		{
			private final Book book;
			private final int  amount;
			
			Default(
				final Book book,
				final int amount
			)
			{
				super();
				this.book   = book;
				this.amount = amount;
			}
			
			@Override
			public Book book()
			{
				return this.book;
			}
			
			@Override
			public int amount()
			{
				return this.amount;
			}
			
			@Override
			public double itemTotal()
			{
				return this.amount * this.book.price();
			}
			
		}
		
	}
	
	public Shop shop();
	
	public Employee employee();
	
	public Customer customer();
	
	public long timestamp();
	
	public Stream<Item> items();
	
	public int itemCount();
	
	public double total();
	
	public static Purchase New(
		final Shop shop,
		final Employee employee,
		final Customer customer,
		final long timestamp,
		final List<Item> items
	)
	{
		return new Default(shop, employee, customer, timestamp, items.toArray(new Item[items.size()]));
	}
	
	public static class Default implements Purchase
	{
		private final Shop       shop;
		private final Employee   employee;
		private final Customer   customer;
		private final long       timestamp;
		private final Item[]     items;
		private transient double total = -1.0;
		
		Default(
			final Shop shop,
			final Employee employee,
			final Customer customer,
			final long timestamp,
			final Item[] items
		)
		{
			super();
			this.shop      = shop;
			this.employee  = employee;
			this.customer  = customer;
			this.timestamp = timestamp;
			this.items     = items;
		}
		
		@Override
		public Shop shop()
		{
			return this.shop;
		}
		
		@Override
		public Employee employee()
		{
			return this.employee;
		}
		
		@Override
		public Customer customer()
		{
			return this.customer;
		}
		
		@Override
		public long timestamp()
		{
			return this.timestamp;
		}
		
		@Override
		public Stream<Item> items()
		{
			return Arrays.stream(this.items);
		}
		
		@Override
		public int itemCount()
		{
			return this.items.length;
		}
		
		@Override
		public double total()
		{
			if(this.total <= 0.0)
			{
				double total = 0.0;
				for(final Item item : this.items)
				{
					total += item.itemTotal();
				}
				this.total = total;
			}
			return this.total;
		}
		
	}
	
}
