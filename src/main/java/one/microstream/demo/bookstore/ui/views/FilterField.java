package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.function.SerializablePredicate;

import one.microstream.demo.bookstore.data.Entity;

public interface FilterField<E extends Entity, F>
{
	public SerializablePredicate<E> filter(SerializablePredicate<E> filter);
}
