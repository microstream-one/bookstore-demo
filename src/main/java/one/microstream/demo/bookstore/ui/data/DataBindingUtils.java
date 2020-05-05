package one.microstream.demo.bookstore.ui.data;

import java.util.function.Function;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;

/**
 * Data binding utilities for the UI layer
 *
 */
public interface DataBindingUtils
{
	/**
	 * Wraps a validation function into a Vaadin {@link Validator} for use with a {@link Binder}.
	 *
	 * @param <T> the entity type
	 * @param validation the validation function
	 * @return a Vaadin {@link Validator}
	 */
	public static <T> Validator<? super T> validator(final Function<T, ?> validation)
	{
		return (value, context) ->
		{
			try
			{
				validation.apply(value);
				return ValidationResult.ok();
			}
			catch(final Exception e)
			{
				return ValidationResult.error(e.getMessage());
			}
		};
	}

	/**
	 * Creates a dummy {@link Setter} for a {@link Binder}.
	 *
	 * @param <B> the bean type
	 * @param <V> the field type
	 * @return a dummy {@link Setter}
	 */
	public static <B, V> Setter<B, V> setterDummy()
	{
		return (bean, fieldvalue) ->
		{
			// no-op
		};
	}
}
