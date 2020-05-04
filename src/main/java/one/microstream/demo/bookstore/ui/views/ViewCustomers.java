package one.microstream.demo.bookstore.ui.views;

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
		this.addGridColumnForName();
		this.addGridColumnsForAddress();
	}

	@Override
	protected Backend<Customer> backend()
	{
		return BookStoreDemo.getInstance().data().customers()::compute;
	}

}
