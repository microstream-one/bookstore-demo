package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;

import one.microstream.demo.bookstore.data.Entity;

public class FilterTextField<E extends Entity> extends TextField implements FilterField<E, String>
{
	private final SerializableFunction<String, SerializablePredicate<E>> filterFactory;

	public FilterTextField(
		final ViewEntity<E> view,
		final String placeholder,
		final SerializableFunction<String, SerializablePredicate<E>> filterFactory
	)
	{
		super();

		this.filterFactory = filterFactory;

		this.setPlaceholder(placeholder);
		this.setClearButtonVisible(true);
		this.setValueChangeMode(ValueChangeMode.TIMEOUT);
		this.addValueChangeListener(event -> view.updateFilter());
	}

	@Override
	public SerializablePredicate<E> filter(final SerializablePredicate<E> filter)
	{
		String value = this.getValue();
		return value != null && (value = value.trim()).length() > 0
			? filter.and(this.filterFactory.apply(value))
			: filter
		;
	}
}
