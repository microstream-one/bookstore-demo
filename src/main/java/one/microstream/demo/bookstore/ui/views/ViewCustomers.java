package one.microstream.demo.bookstore.ui.views;

import java.util.HashMap;
import java.util.Map;

import com.flowingcode.vaadin.addons.ironicons.IronIcons;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Customer;
import one.microstream.demo.bookstore.ui.data.BookStoreDataProvider.Backend;

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
		this.addGridColumn(Customer::customerId, "ID");
		this.addGridColumnForName();
		this.addGridColumnsForAddress();

		final Button showPurchasesButton = new Button(
			"Show Purchases",
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
	protected Backend<Customer> backend()
	{
		return BookStoreDemo.getInstance().data().customers()::compute;
	}

	private void showPurchases(final Customer customer)
	{
		final Map<String, String> params = new HashMap<>();
		params.put("customer", Integer.toString(customer.customerId()));
		this.getUI().get().navigate("purchases", QueryParameters.simple(params));
	}

}
