
package one.microstream.demo.bookstore.data;

public class DataMetrics
{
	private final int bookCount   ;
	private final int countryCount;
	private final int shopCount   ;

	public DataMetrics(
		final int bookCount   ,
		final int countryCount,
		final int shopCount
	)
	{
		super();
		this.bookCount     = bookCount   ;
		this.countryCount  = countryCount;
		this.shopCount     = shopCount   ;
	}

	public int bookCount()
	{
		return this.bookCount;
	}

	public int countryCount()
	{
		return this.countryCount;
	}

	public int shopCount()
	{
		return this.shopCount;
	}

	@Override
	public String toString()
	{
		return this.bookCount   + " books, "
			+ this.shopCount    + " shops in "
			+ this.countryCount + " countries";
	}

}
