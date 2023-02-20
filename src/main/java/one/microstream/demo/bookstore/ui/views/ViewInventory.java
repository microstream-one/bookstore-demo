package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.Route;
import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Book;
import one.microstream.demo.bookstore.data.InventoryItem;
import one.microstream.demo.bookstore.data.Shop;

import java.util.stream.Stream;

/**
 * View to display {@link InventoryItem}s.
 *
 */
@Route(value = "inventory", layout = RootLayout.class)
@SuppressWarnings("serial")
public class ViewInventory extends ViewEntity<InventoryItem>
{
	private FilterComboBox<InventoryItem, Shop> shopFilter;
	private FilterComboBox<InventoryItem, Book> bookFilter;

	public ViewInventory()
	{
		super();
	}

	public void filterBy(Book book) {
		bookFilter.setValue(book);
	}

	public void filterBy(Shop shop) {
		shopFilter.setValue(shop);
	}


	@Override
	protected void createUI()
	{
		shopFilter = this.addGridColumnWithDynamicFilter("shop"  , InventoryItem::shop  );
		bookFilter = this.addGridColumnWithDynamicFilter("book"  , InventoryItem::book  );
		this.addGridColumn                              ("amount", InventoryItem::amount);
	}

	@Override
	public <R> R compute(SerializableFunction<Stream<InventoryItem>, R> function) {
		return BookStoreDemo.getInstance().data().shops().computeInventory(function);
	}

}
