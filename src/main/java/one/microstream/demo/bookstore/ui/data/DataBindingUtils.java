package one.microstream.demo.bookstore.ui.data;

import java.util.function.Function;

import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;

public interface DataBindingUtils
{
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

	public static <B, V> Setter<B, V> setterDummy()
	{
		return (bean, fieldvalue) ->
		{
			// no-op
		};
	}
}
