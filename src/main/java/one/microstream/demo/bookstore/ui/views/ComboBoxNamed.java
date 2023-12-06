package one.microstream.demo.bookstore.ui.views;

import java.util.Collection;

import com.vaadin.flow.component.combobox.ComboBox;

import one.microstream.demo.bookstore.data.Named;

/**
 * {@link ComboBox} for {@link Named} entities.
 *
 * @param <T> the entity type
 */
public class ComboBoxNamed<T extends Named> extends ComboBox<T>
{
	public ComboBoxNamed()
	{
		super();
		this.init();
	}

	public ComboBoxNamed(final Collection<T> items)
	{
		super();
		this.setItems(items);
		this.init();
	}

	public ComboBoxNamed(final String label)
	{
		super(label);
		this.init();
	}

	public ComboBoxNamed(final String label, final Collection<T> items)
	{
		super(label, items);
		this.init();
	}

	private void init()
	{
		this.setItemLabelGenerator(Named::name);
	}

	public ComboBoxNamed<T> withItems(final Collection<T> items)
	{
		this.setItems(items);
		return this;
	}

}
