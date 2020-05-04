package one.microstream.demo.bookstore.ui.views;

import one.microstream.demo.bookstore.data.Named;

public abstract class ViewNamed<E extends Named> extends ViewEntity<E>
{
	protected ViewNamed()
	{
		super();
	}

	protected void addGridColumnForName()
	{
		this.addGridColumnWithTextFilter(Named::name, "Name");
	}
}
