package one.microstream.demo.bookstore.ui.views;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Book;

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
		this.addGridColumnWithTextFilter   ("Title"    , Book::title    );
		this.addGridColumnWithDynamicFilter("Author"   , Book::author   );
		this.addGridColumnWithDynamicFilter("Genre"    , Book::genre    );
		this.addGridColumnWithDynamicFilter("Publisher", Book::publisher);
		this.addGridColumnWithDynamicFilter("Language" , Book::language );
		this.addGridColumnWithTextFilter   ("ISBN"     , Book::isbn13   );

		final Button createBookButton = new Button(
			"New Book",
			VaadinIcon.PLUS_CIRCLE.create(),
			event -> this.openCreateBookDialog()
		);

		this.add(new HorizontalLayout(createBookButton));
	}

	private void openCreateBookDialog()
	{
		DialogBookCreate.open(book ->
		{
			BookStoreDemo.getInstance().data().books().add(book);
			this.updateDataProvider();
		});
	}

	@Override
	protected List<Book> entities()
	{
		return BookStoreDemo.getInstance().data().books().all();
	}

}
