package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.function.SerializablePredicate;

/**
 * A UI component which is used for filtering.
 *
 * @param <E> the entity type
 * @param <F> the field type
 */
public interface FilterField<E, F>
{
	public SerializablePredicate<E> filter(SerializablePredicate<E> filter);
}
