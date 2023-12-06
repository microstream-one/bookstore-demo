
package one.microstream.demo.bookstore.data;

import static one.microstream.X.notNull;
import static one.microstream.demo.bookstore.util.ValidationUtils.requireNonEmpty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.money.MonetaryAmount;

/**
 * Purchase entity which holds a {@link Shop}, {@link Employee},
 * {@link Customer}, timestamp and {@link PurchaseItem}s.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public class Purchase
{
	private final Shop               shop     ;
	private final Employee           employee ;
	private final Customer           customer ;
	private final LocalDateTime      timestamp;
	private final List<PurchaseItem> items    ;
	private transient MonetaryAmount total    ;

	/**
	 * Constructor to create a new {@link Purchase} instance.
	 *
	 * @param shop not <code>null</code>
	 * @param employee not <code>null</code>
	 * @param customer not <code>null</code>
	 * @param timestamp not <code>null</code>
	 * @param items not empty
	 */
	public Purchase(
		final Shop               shop     ,
		final Employee           employee ,
		final Customer           customer ,
		final LocalDateTime      timestamp,
		final List<PurchaseItem> items
	)
	{
		super();
		this.shop      = notNull(shop);
		this.employee  = notNull(employee);
		this.customer  = notNull(customer);
		this.timestamp = notNull(timestamp);
		this.items     = new ArrayList<>(requireNonEmpty(items, () -> "at least one item required in purchase"));
	}

	/**
	 * Get the shop the purchase was made in
	 *
	 * @return the shop
	 */
	public Shop shop()
	{
		return this.shop;
	}

	/**
	 * Get the employee who sold
	 *
	 * @return the employee
	 */
	public Employee employee()
	{
		return this.employee;
	}

	/**
	 * Get the customer who made the purchase
	 *
	 * @return the customer
	 */
	public Customer customer()
	{
		return this.customer;
	}

	/**
	 * The timestamp the purchase was made at
	 *
	 * @return the timestamp
	 */
	public LocalDateTime timestamp()
	{
		return this.timestamp;
	}

	/**
	 * Get all {@link PurchaseItem}s of this purchase
	 *
	 * @return a {@link Stream} of {@link PurchaseItem}s
	 */
	public Stream<PurchaseItem> items()
	{
		return this.items.stream();
	}

	/**
	 * Get all {@link PurchaseItem}s of this purchase
	 *
	 * @return a {@link List} of {@link PurchaseItem}s
	 */
	public List<PurchaseItem> itemsList()
	{
		return new ArrayList<>(this.items);
	}

	public int itemCount()
	{
		return this.items.size();
	}

	/**
	 * Computes the total of this purchase (sum of {@link PurchaseItem#itemTotal()})
	 *
	 * @return the total amount
	 */
	public MonetaryAmount total()
	{
		if(this.total  == null)
		{
			MonetaryAmount total = null;
			for(final PurchaseItem item : this.items)
			{
				total = total == null
					? item.itemTotal()
					: total.add(item.itemTotal());
			}
			this.total = total;
		}
		return this.total;
	}

}
