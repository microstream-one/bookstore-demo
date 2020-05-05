package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.component.grid.Grid;

import one.microstream.demo.bookstore.data.NamedWithAddress;

/**
 * Abstract view to display {@link NamedWithAddress} entities in a {@link Grid}.
 *
 * @param <E> the entity type
 */
@SuppressWarnings("serial")
public abstract class ViewNamedWithAddress<E extends NamedWithAddress> extends ViewNamed<E>
{
	protected ViewNamedWithAddress()
	{
		super();
	}

	protected void addGridColumnsForAddress()
	{
		this.addGridColumnWithTextFilter   ("Address",  e -> e.address().address()               );
		this.addGridColumnWithTextFilter   ("Address2", e -> e.address().address2()              );
		this.addGridColumnWithTextFilter   ("Zipcode",  e -> e.address().zipCode()               );
		this.addGridColumnWithDynamicFilter("City",     e -> e.address().city()                  );
		this.addGridColumnWithDynamicFilter("State",    e -> e.address().city().state()          );
		this.addGridColumnWithDynamicFilter("Country",  e -> e.address().city().state().country());
	}
}
