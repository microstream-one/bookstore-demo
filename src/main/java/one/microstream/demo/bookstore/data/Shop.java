
package one.microstream.demo.bookstore.data;

import static one.microstream.demo.bookstore.util.LazyUtils.clearIfStored;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import one.microstream.reference.Lazy;

/**
 * Shop entity which holds a name, {@link Address}, {@link Employee}s and an {@link Inventory}.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public class Shop extends NamedWithAddress
{
	private final List<Employee>  employees;
	private final Lazy<Inventory> inventory;
	
	/**
	 * Constructor to create a new {@link Shop} instance.
	 *
	 * @param name not empty, {@link Named.Validation#validateName(String)}
	 * @param address not <code>null</code>
	 */
	public Shop(
		final String  name   ,
		final Address address
	)
	{
		super(name, address);
		
		this.employees = new ArrayList<>();
		this.inventory = Lazy.Reference(new Inventory());
	}
	
	/**
	 * Package-private constructor used by {@link RandomDataGenerator}
	 */
	Shop(
		final String         name     ,
		final Address        address  ,
		final List<Employee> employees,
		final Inventory      inventory
	)
	{
		super(name, address);
		this.employees = new ArrayList<>(employees);
		this.inventory = Lazy.Reference(inventory);
	}
	
	/**
	 * Get the employees.
	 *
	 * @return a {@link Stream} of {@link Employee}s
	 */
	public Stream<Employee> employees()
	{
		return this.employees.stream();
	}

	/**
	 * Get the inventory.
	 *
	 * @return the inventory
	 */
	public Inventory inventory()
	{
		return this.inventory.get();
	}

	/**
	 * Clears all {@link Lazy} references held by this shop.
	 * This frees the used memory but you do not lose the persisted data. It is loaded again on demand.
	 */
	public void clear()
	{
		clearIfStored(this.inventory);
	}

}
