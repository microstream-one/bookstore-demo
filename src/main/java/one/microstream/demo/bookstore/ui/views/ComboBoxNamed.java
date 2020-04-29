package one.microstream.demo.bookstore.ui.views;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.provider.DataProvider;

import one.microstream.demo.bookstore.data.Named;

public class ComboBoxNamed<T extends Named> extends ComboBox<T>
{
	public ComboBoxNamed()
	{
		super();
	}
	public ComboBoxNamed(final Collection<T> items)
	{
		super();
		this.setItems(items);
	}

	public ComboBoxNamed(final String label)
	{
		super(label);
	}

	public ComboBoxNamed(final String label, final Collection<T> items)
	{
		super(label, items);
	}

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
