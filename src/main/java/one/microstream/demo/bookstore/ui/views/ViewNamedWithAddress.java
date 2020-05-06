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
		this.addGridColumnWithTextFilter   (this.getTranslation("address1"), e -> e.address().address()               );
		this.addGridColumnWithTextFilter   (this.getTranslation("address2"), e -> e.address().address2()              );
		this.addGridColumnWithTextFilter   (this.getTranslation("zipcode"),  e -> e.address().zipCode()               );
		this.addGridColumnWithDynamicFilter(this.getTranslation("city"),     e -> e.address().city()                  );
		this.addGridColumnWithDynamicFilter(this.getTranslation("state"),    e -> e.address().city().state()          );
		this.addGridColumnWithDynamicFilter(this.getTranslation("country"),  e -> e.address().city().state().country());
	}
}
