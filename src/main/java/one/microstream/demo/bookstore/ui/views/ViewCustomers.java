package one.microstream.demo.bookstore.ui.views;

import java.util.List;

import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Customer;

@Route(value = "customers", layout = RootLayout.class)
@SuppressWarnings("serial")
public class ViewCustomers extends ViewNamedWithAddress<Customer>
{
	public ViewCustomers(
		final BookStoreDemo bookStoreDemo
	)
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
	protected List<Customer> entities()
	{
		return BookStoreDemo.getInstance().data().customers().all();
	}
}
