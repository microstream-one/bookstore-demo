package one.microstream.demo.bookstore.ui.views;

import java.util.List;
import java.util.Map;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Book;
import one.microstream.demo.bookstore.data.InventoryItem;
import one.microstream.demo.bookstore.data.Shop;
import one.microstream.demo.bookstore.ui.data.BookStoreDataProvider.Backend;

@Route(value = "inventory", layout = RootLayout.class)
@SuppressWarnings("serial")
public class ViewInventory extends ViewEntity<InventoryItem> implements HasUrlParameter<String>
{
	Shop shop;
	Book book;

	public ViewInventory()
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
		final List<String> bookParams = params.get("book");
		if(bookParams != null && bookParams.size() == 1)
		{
			this.book = BookStoreDemo.getInstance().data().books().ofIsbn13(bookParams.get(0));
		}
	}

	@Override
	protected void createUI()
	{
		this.addGridColumnWithDynamicFilter("Shop", InventoryItem::shop, this.shop);
		this.addGridColumnWithDynamicFilter("Book", InventoryItem::book, this.book);
		this.addGridColumn("Amount", InventoryItem::amount);
	}

	@Override
	protected Backend<InventoryItem> backend()
	{
		return BookStoreDemo.getInstance().data().shops()::computeInventory;
	}

}
