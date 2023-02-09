package one.microstream.demo.bookstore.ui.views;

import com.flowingcode.vaadin.addons.ironicons.IronIcons;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.Route;
import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Customer;
import one.microstream.demo.bookstore.data.Customers;

import java.util.stream.Stream;

/**
 * View to display {@link Customers}.
 *
 */
@Route(value = "customers", layout = RootLayout.class)
@SuppressWarnings("serial")
public class ViewCustomers extends ViewNamedWithAddress<Customer>
{
	public ViewCustomers()
	{
		super();
	}

	@Override
	protected void createUI()
	{
		this.addGridColumn("id", Customer::customerId);
		this.addGridColumnForName();
		this.addGridColumnsForAddress();

		final Button showPurchasesButton = new Button(
			this.getTranslation("showPurchases"),
			IronIcons.SHOPPING_BASKET.create(),
			event -> this.showPurchases(this.getSelectedEntity())
		);

		showPurchasesButton.setEnabled(false);
		this.grid.addSelectionListener(event -> {
			final boolean b = event.getFirstSelectedItem().isPresent();
			showPurchasesButton.setEnabled(b);
		});

		this.add(new HorizontalLayout(showPurchasesButton));
	}

	@Override
	public <R> R compute(SerializableFunction<Stream<Customer>, R> function) {
		return BookStoreDemo.getInstance().data().customers().compute(function);
	}

	private void showPurchases(final Customer customer)
	{
		getUI().get().navigate(ViewPurchases.class).get().filterBy(customer);
	}

}
