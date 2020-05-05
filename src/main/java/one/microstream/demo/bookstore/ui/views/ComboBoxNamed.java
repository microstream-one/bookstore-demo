package one.microstream.demo.bookstore.ui.views;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.DataProvider;

import one.microstream.demo.bookstore.data.Named;

/**
 * {@link ComboBox} for {@link Named} entities.
 *
 * @param <T> the entity type
 */
@SuppressWarnings("serial")
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

	@Override
	public void setItems(final Collection<T> items)
	{
		this.setDataProvider(
			DataProvider.ofCollection(items),
			filterText -> entity -> StringUtils.containsIgnoreCase(entity.name(), filterText)
		);
	}
}
