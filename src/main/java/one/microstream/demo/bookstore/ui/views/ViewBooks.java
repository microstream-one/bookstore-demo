package one.microstream.demo.bookstore.ui.views;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Book;
import one.microstream.demo.bookstore.ui.data.BookStoreDataProvider.Backend;

@Route(value = "books", layout = RootLayout.class)
@SuppressWarnings("serial")
public class ViewBooks extends ViewEntity<Book>
{
	public ViewBooks()
	{
		super();
	}

	@Override
	protected void createUI()
	{
		this.addGridColumnWithTextFilter   (Book::title    , "Title"    );
		this.addGridColumnWithDynamicFilter(Book::author   , "Author"   );
		this.addGridColumnWithDynamicFilter(Book::genre    , "Genre"    );
		this.addGridColumnWithDynamicFilter(Book::publisher, "Publisher");
		this.addGridColumnWithDynamicFilter(Book::language , "Language" );
		this.addGridColumnWithTextFilter   (Book::isbn13     , "ISBN"   );

		final Button showInventoryButton = new Button(
			"Show Inventory",
			VaadinIcon.STOCK.create(),
			event -> this.showInventory(this.getSelectedEntity())
		);
		showInventoryButton.setEnabled(false);
		this.grid.addSelectionListener(event -> {
			final boolean b = event.getFirstSelectedItem().isPresent();
			showInventoryButton.setEnabled(b);
		});

		final Button createBookButton = new Button(
			"New Book",
			VaadinIcon.PLUS_CIRCLE.create(),
			event -> this.openCreateBookDialog()
		);

		this.add(new HorizontalLayout(showInventoryButton, createBookButton));
	}

	private void showInventory(final Book book)
	{
		final Map<String, String> params = new HashMap<>();
		params.put("book", book.isbn13());
		this.getUI().get().navigate("inventory", QueryParameters.simple(params));
	}

	private void openCreateBookDialog()
	{
		DialogBookCreate.open(book ->
		{
			BookStoreDemo.getInstance().data().books().add(book);
			this.refresh();
		});
	}

	@Override
	protected Backend<Book> backend()
	{
		return BookStoreDemo.getInstance().data().books()::compute;
	}

}
