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
		this.addGridColumnWithTextFilter   ("address1", e -> e.address().address()               );
		this.addGridColumnWithTextFilter   ("address2", e -> e.address().address2()              );
		this.addGridColumnWithTextFilter   ("zipcode",  e -> e.address().zipCode()               );
		this.addGridColumnWithDynamicFilter("city",     e -> e.address().city()                  );
		this.addGridColumnWithDynamicFilter("state",    e -> e.address().city().state()          );
		this.addGridColumnWithDynamicFilter("country",  e -> e.address().city().state().country());
	}
}
