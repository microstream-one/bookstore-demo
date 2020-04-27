
package one.microstream.demo.bookstore.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import one.microstream.reference.Lazy;


public interface Shop extends NamedWithAddress
{
	public Stream<Employee> employees();
	
	public Inventory inventory();
	
	public void clear();
	
	public static class Default extends NamedWithAddress.Abstract implements Shop
	{
		private final List<Employee>                employees;
		private final Lazy<Inventory>               inventory;
		
		Default(
			final String name,
			final Address address
		)
		{
			super(name, address);
			this.employees       = new ArrayList<>();
			this.inventory       = Lazy.Reference(new Inventory.Default());
		}
		
		Default(
			final String name,
			final Address address,
			final List<Employee> employees,
			final Inventory inventory
		)
		{
			super(name, address);
			this.employees       = employees;
			this.inventory       = Lazy.Reference(inventory);
		}
		
		@Override
		public Stream<Employee> employees()
		{
			return this.employees.stream();
		}
		
		@Override
		public Inventory inventory()
		{
			return this.inventory.get();
		}
		
		@Override
		public void clear()
		{
			if(this.inventory != null && this.inventory.isStored())
			{
				this.inventory.clear();
			}
		}
		
	}
	
}
