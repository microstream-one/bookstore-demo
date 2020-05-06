package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Book;
import one.microstream.demo.bookstore.data.InventoryItem;
import one.microstream.demo.bookstore.data.Shop;
import one.microstream.demo.bookstore.ui.data.BookStoreDataProvider.Backend;

/**
 * View to display {@link InventoryItem}s.
 *
 */
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
		@OptionalParameter final String parameter
	)
	{
		final String shopParam = getQueryParameter(event, "shop");
		if(shopParam != null)
		{
			this.shop = BookStoreDemo.getInstance().data().shops().ofName(shopParam);
		}

		final String bookParam = getQueryParameter(event, "book");
		if(bookParam != null)
		{
			this.book = BookStoreDemo.getInstance().data().books().ofIsbn13(bookParam);
		}
	}

	@Override
	protected void createUI()
	{
		this.addGridColumnWithDynamicFilter(this.getTranslation("shop")  , InventoryItem::shop  , this.shop);
		this.addGridColumnWithDynamicFilter(this.getTranslation("book")  , InventoryItem::book  , this.book);
		this.addGridColumn                 (this.getTranslation("amount"), InventoryItem::amount           );
	}

	@Override
	protected Backend<InventoryItem> backend()
	{
		return BookStoreDemo.getInstance().data().shops()::computeInventory;
	}

}
