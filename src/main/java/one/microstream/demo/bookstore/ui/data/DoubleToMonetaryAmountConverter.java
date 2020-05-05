package one.microstream.demo.bookstore.ui.data;

import javax.money.MonetaryAmount;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import one.microstream.demo.bookstore.BookStoreDemo;

/**
 * A {@link Converter} that converts from {@link Double} to {@link MonetaryAmount} and back.
 *
 */
@SuppressWarnings("serial")
public interface DoubleToMonetaryAmountConverter extends Converter<Double, MonetaryAmount>
{
	/**
	 * Pseudo-constructor method to create a new {@link DoubleToMonetaryAmountConverter}
	 * instance with default implementation.
	 *
	 * @return a new {@link DoubleToMonetaryAmountConverter}
	 */
	public static DoubleToMonetaryAmountConverter New()
	{
		return new Default();
	}


	/**
	 * Default implementation of the {@link DoubleToMonetaryAmountConverter} interface.
	 *
	 */
	public class Default implements DoubleToMonetaryAmountConverter
	{
		Default()
		{
			super();
		}

		@Override
		public Result<MonetaryAmount> convertToModel(final Double value, final ValueContext context)
		{
			return Result.ok(value != null
				? BookStoreDemo.money(value)
				: null
			);
		}

		@Override
		public Double convertToPresentation(final MonetaryAmount value, final ValueContext context)
		{
			return value != null
				? value.getNumber().doubleValue()
				: null
			;
		}

	}

}
