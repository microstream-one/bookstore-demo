package one.microstream.demo.bookstore.ui.data;

import javax.money.MonetaryAmount;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import one.microstream.demo.bookstore.BookStoreDemo;

@SuppressWarnings("serial")
public class DoubleToMonetaryAmountConverter implements Converter<Double, MonetaryAmount>
{
	public DoubleToMonetaryAmountConverter()
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
