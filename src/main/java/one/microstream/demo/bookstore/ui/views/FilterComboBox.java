package one.microstream.demo.bookstore.ui.views;

import java.util.function.Function;

import com.vaadin.flow.function.SerializablePredicate;

import one.microstream.demo.bookstore.data.Named;

public class FilterComboBox<E, F extends Named> extends ComboBoxNamed<F> implements FilterField<E, F>
{
	private final Function<F, SerializablePredicate<E>> filterFactory;

	public FilterComboBox(
		final Function<F, SerializablePredicate<E>> filterFactory
	)
	{
		super();

		this.filterFactory = filterFactory;

		this.setPlaceholder("Filter");
		this.setClearButtonVisible(true);
	}

	@Override
	public SerializablePredicate<E> filter(final SerializablePredicate<E> filter)
	{
		final F value = this.getValue();
		return value != null
			? filter.and(this.filterFactory.apply(value))
			: filter
		;
	}
}
