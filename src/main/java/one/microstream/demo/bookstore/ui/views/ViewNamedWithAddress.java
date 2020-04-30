package one.microstream.demo.bookstore.ui.views;

import one.microstream.demo.bookstore.data.NamedWithAddress;

public abstract class ViewNamedWithAddress<E extends NamedWithAddress> extends ViewNamed<E>
{
	protected ViewNamedWithAddress()
	{
		super();
	}

	protected void addGridColumnsForAddress()
	{
		this.addGridColumnWithTextFilter   ("Address" , e -> e.address().address()               );
		this.addGridColumnWithTextFilter   ("Address2", e -> e.address().address2()              );
		this.addGridColumnWithTextFilter   ("Zipcode" , e -> e.address().zipCode()               );
		this.addGridColumnWithDynamicFilter("City"    , e -> e.address().city()                  );
		this.addGridColumnWithDynamicFilter("State"   , e -> e.address().city().state()          );
		this.addGridColumnWithDynamicFilter("Country" , e -> e.address().city().state().country());
	}
}
