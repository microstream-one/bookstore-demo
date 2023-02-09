package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.Route;
import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Book;
import one.microstream.demo.bookstore.data.Books;

import java.util.stream.Stream;

/**
 * View to display and modify {@link Books}.
 *
 */
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
		this.addGridColumnWithTextFilter   ("title"    , Book::title    );
		this.addGridColumnWithDynamicFilter("author"   , Book::author   );
		this.addGridColumnWithDynamicFilter("genre"    , Book::genre    );
		this.addGridColumnWithDynamicFilter("publisher", Book::publisher);
		this.addGridColumnWithDynamicFilter("language" , Book::language );
		this.addGridColumnWithTextFilter   ("isbn13"   , Book::isbn13   );

		final Button showInventoryButton = new Button(
			this.getTranslation("showInventory"),
			VaadinIcon.STOCK.create(),
			event -> this.showInventory(this.getSelectedEntity())
		);
		showInventoryButton.setEnabled(false);
		this.grid.addSelectionListener(event -> {
			final boolean b = event.getFirstSelectedItem().isPresent();
			showInventoryButton.setEnabled(b);
		});

		final Button createBookButton = new Button(
			this.getTranslation("createBook"),
			VaadinIcon.PLUS_CIRCLE.create(),
			event -> this.openCreateBookDialog()
		);

		this.add(new HorizontalLayout(showInventoryButton, createBookButton));
	}

	private void showInventory(final Book book)
	{
		getUI().get().navigate(ViewInventory.class).get().filterBy(book);
	}

	private void openCreateBookDialog()
	{
		DialogBookCreate.open(book ->
		{
			BookStoreDemo.getInstance().data().books().add(book);
			listEntities();
		});
	}



	@Override
	public <R> R compute(SerializableFunction<Stream<Book>, R> function) {
		return BookStoreDemo.getInstance().data().books().compute(function);
	}
}
