package one.microstream.demo.bookstore.ui.views;

import static one.microstream.X.notNull;

import java.util.function.Function;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.function.SerializablePredicate;

import one.microstream.demo.bookstore.data.Named;

/**
 * Filter {@link ComboBox} for {@link Named} entities.
 *
 * @param <E> the entity type
 * @param <F> the field type
 */
@SuppressWarnings("serial")
public class FilterComboBox<E, F extends Named> extends ComboBoxNamed<F> implements FilterField<E, F>
{
	private final Function<F, SerializablePredicate<E>> filterFactory;

	public FilterComboBox(
		final Function<F, SerializablePredicate<E>> filterFactory
	)
	{
		super();

		this.filterFactory = notNull(filterFactory);

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
