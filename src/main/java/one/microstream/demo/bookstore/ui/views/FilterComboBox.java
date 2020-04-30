package one.microstream.demo.bookstore.ui.views;

import java.util.function.Function;

import com.vaadin.flow.function.SerializablePredicate;

import one.microstream.demo.bookstore.data.Entity;
import one.microstream.demo.bookstore.data.Named;

public class FilterComboBox<E extends Entity, F extends Named> extends ComboBoxNamed<F> implements FilterField<E, F>
{
	private final Function<F, SerializablePredicate<E>> filterFactory;

	public FilterComboBox(
		final ViewEntity<E> view,
		final String placeholder,
		final Function<F, SerializablePredicate<E>> filterFactory
	)
	{
		super();

		this.filterFactory = filterFactory;

		this.setPlaceholder(placeholder);
		this.setClearButtonVisible(true);
		this.addValueChangeListener(event -> view.updateFilter());
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
