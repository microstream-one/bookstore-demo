package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.component.grid.Grid;

import one.microstream.demo.bookstore.data.Named;

/**
 * Abstract view to display {@link Named} entities in a {@link Grid}.
 *
 * @param <E> the entity type
 */
@SuppressWarnings("serial")
public abstract class ViewNamed<E extends Named> extends ViewEntity<E>
{
	protected ViewNamed()
	{
		super();
	}

	protected void addGridColumnForName()
	{
		this.addGridColumnWithTextFilter(this.getTranslation("name"), Named::name);
	}
}
