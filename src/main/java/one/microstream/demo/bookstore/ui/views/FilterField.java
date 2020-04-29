package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.function.SerializablePredicate;

public interface FilterField<E, F>
{
	public SerializablePredicate<E> filter(SerializablePredicate<E> filter);
}
