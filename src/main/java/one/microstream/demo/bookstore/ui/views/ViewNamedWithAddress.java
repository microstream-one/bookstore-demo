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
		this.addGridColumnWithTextFilter   (e -> e.address().address()               , "Address" );
		this.addGridColumnWithTextFilter   (e -> e.address().address2()              , "Address2");
		this.addGridColumnWithTextFilter   (e -> e.address().zipCode()               , "Zipcode" );
		this.addGridColumnWithDynamicFilter(e -> e.address().city()                  , "City"    );
		this.addGridColumnWithDynamicFilter(e -> e.address().city().state()          , "State"   );
		this.addGridColumnWithDynamicFilter(e -> e.address().city().state().country(), "Country" );
	}
}
