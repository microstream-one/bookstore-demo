package one.microstream.demo.bookstore.ui.views;

import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import com.google.common.collect.Range;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Customer;
import one.microstream.demo.bookstore.data.Purchase;
import one.microstream.demo.bookstore.data.Shop;
import one.microstream.demo.bookstore.ui.data.BookStoreDataProvider.Backend;

@Route(value = "purchases", layout = RootLayout.class)
@SuppressWarnings("serial")
public class ViewPurchases extends ViewEntity<Purchase> implements HasUrlParameter<String>
{
	int      year = Year.now().getValue();
	Shop     shop;
	Customer customer;

	public ViewPurchases()
	{
		super();
	}

	@Override
	public void setParameter(
		final BeforeEvent event,
		@OptionalParameter final String parameter)
	{
		final Map<String, List<String>> params = event.getLocation().getQueryParameters().getParameters();
		final List<String> shopParams = params.get("shop");
		if(shopParams != null && shopParams.size() == 1)
		{
			this.shop = BookStoreDemo.getInstance().data().shops().ofName(shopParams.get(0));
		}
		final List<String> customerParams = params.get("customer");
		if(customerParams != null && customerParams.size() == 1)
		{
			try
			{
				final int customerId = Integer.parseInt(customerParams.get(0));
				this.customer = BookStoreDemo.getInstance().data().customers().ofId(customerId);
			}
			catch(final NumberFormatException e)
			{
			}
		}
	}

	@Override
	protected void createUI()
	{
		this.addGridColumnWithDynamicFilter(Purchase::shop, "Shop", this.shop);
		this.addGridColumnWithDynamicFilter(Purchase::employee, "Employee");
		this.addGridColumnWithDynamicFilter(Purchase::customer, "Customer", this.customer);
		this.addGridColumn(Purchase::timestamp, "Timestamp");
		this.addGridColumn(Purchase::total, "Total");

		final Range<Integer> years = BookStoreDemo.getInstance().data().purchases().years();

		final IntegerField yearField = new IntegerField();
		yearField.setHasControls(true);
		yearField.setMin(years.lowerEndpoint());
		yearField.setMax(years.upperEndpoint());
		yearField.setValue(this.year);
		yearField.addValueChangeListener(event -> {
			this.year = event.getValue();
			this.refresh();
		});

		final HorizontalLayout bar = new HorizontalLayout(new Label("Year"), yearField);
		bar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		this.add(bar);
	}

	@Override
	protected Backend<Purchase> backend()
	{
		return this::compute;
	}

	private <R> R compute(final Function<Stream<Purchase>, R> function)
	{
		return BookStoreDemo.getInstance().data().purchases().computeByYear(
			this.year,
			function
		);
	}

}
