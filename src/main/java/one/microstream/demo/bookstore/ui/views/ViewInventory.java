package one.microstream.demo.bookstore.ui.views;

import java.util.stream.Stream;

import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Book;
import one.microstream.demo.bookstore.data.InventoryItem;
import one.microstream.demo.bookstore.data.Shop;

/**
 * View to display {@link InventoryItem}s.
 *
 */
@Route(value = "inventory", layout = RootLayout.class)
public class ViewInventory extends ViewEntity<InventoryItem>
{
	private FilterComboBox<InventoryItem, Shop> shopFilter;
	private FilterComboBox<InventoryItem, Book> bookFilter;

	public ViewInventory()
	{
		super();
	}

	public void filterBy(final Book book)
	{
		this.bookFilter.setValue(book);
	}

	public void filterBy(final Shop shop)
	{
		this.shopFilter.setValue(shop);
	}


	@Override
	protected void createUI()
	{
		this.shopFilter = this.addGridColumnWithDynamicFilter("shop"  , InventoryItem::shop  );
		this.bookFilter = this.addGridColumnWithDynamicFilter("book"  , InventoryItem::book  );
		this.addGridColumn                              ("amount", InventoryItem::amount);
	}

	@Override
	public <R> R compute(final SerializableFunction<Stream<InventoryItem>, R> function)
	{
		return BookStoreDemo.getInstance().data().shops().computeInventory(function);
	}

}
