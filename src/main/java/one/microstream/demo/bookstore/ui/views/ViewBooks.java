package one.microstream.demo.bookstore.ui.views;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Author;
import one.microstream.demo.bookstore.data.Book;
import one.microstream.demo.bookstore.data.Genre;
import one.microstream.demo.bookstore.data.Publisher;
import one.microstream.demo.bookstore.ui.data.DataBindingUtils;

@Route(value = "books", layout = RootLayout.class)
@SuppressWarnings("serial")
public class ViewBooks extends VerticalLayout
{
	final BookStoreDemo                                                     bookStoreDemo;
	final TextField                                                         filterFieldTitle;
	final ComboBox<Author>                                                  filterComboAutor;
	final ComboBox<Genre>                                                   filterComboGenre;
	final ComboBox<Publisher>                                               filterComboPublisher;
	final TextField                                                         filterFieldIsbn;
	final Grid<Book>                                                        grid;
	ConfigurableFilterDataProvider<Book, Void, SerializablePredicate<Book>> dataProvider;

	public ViewBooks(
		final BookStoreDemo bookStoreDemo
	)
	{
		super();

		this.bookStoreDemo = bookStoreDemo;

		this.filterFieldTitle = new TextField();
		this.filterFieldTitle.setPlaceholder("Title");
		this.filterFieldTitle.setClearButtonVisible(true);
		this.filterFieldTitle.setValueChangeMode(ValueChangeMode.TIMEOUT);
		this.filterFieldTitle.addValueChangeListener(event -> this.updateFilter());

		this.filterComboAutor = new ComboBox<>();
		this.filterComboAutor.setPlaceholder("Author");
		this.filterComboAutor.setClearButtonVisible(true);
		this.filterComboAutor.addValueChangeListener(event -> this.updateFilter());
		DataBindingUtils.configureAuthorsComboBox(bookStoreDemo, this.filterComboAutor);

		this.filterComboGenre = new ComboBox<>();
		this.filterComboGenre.setPlaceholder("Genre");
		this.filterComboGenre.setClearButtonVisible(true);
		this.filterComboGenre.addValueChangeListener(event -> this.updateFilter());
		DataBindingUtils.configureGenresComboBox(bookStoreDemo, this.filterComboGenre);

		this.filterComboPublisher = new ComboBox<>();
		this.filterComboPublisher.setPlaceholder("Publisher");
		this.filterComboPublisher.setClearButtonVisible(true);
		this.filterComboPublisher.addValueChangeListener(event -> this.updateFilter());
		DataBindingUtils.configurePublishersComboBox(bookStoreDemo, this.filterComboPublisher);

		this.filterFieldIsbn = new TextField();
		this.filterFieldIsbn.setPlaceholder("ISBN");
		this.filterFieldIsbn.setClearButtonVisible(true);
		this.filterFieldIsbn.setValueChangeMode(ValueChangeMode.TIMEOUT);
		this.filterFieldIsbn.addValueChangeListener(event -> this.updateFilter());

		this.grid = new Grid<>();
		this.grid.addColumn(Book::title)
			.setHeader(this.filterFieldTitle)
			.setResizable(true)
			.setSortable(true);
		this.grid.addColumn(b -> b.author().name())
			.setHeader(this.filterComboAutor)
			.setResizable(true)
			.setSortable(true);
		this.grid.addColumn(b -> b.genre().name())
			.setHeader(this.filterComboGenre)
			.setResizable(true)
			.setSortable(true);
		this.grid.addColumn(b -> b.publisher().name())
			.setHeader(this.filterComboPublisher)
			.setResizable(true)
			.setSortable(true);
		this.grid.addColumn(Book::isbn13)
			.setHeader(this.filterFieldIsbn)
			.setResizable(true)
			.setSortable(true);
		this.grid.setMultiSort(true);
		this.grid.setSizeFull();

		final Button createBookButton = new Button("New Book", VaadinIcon.PLUS_CIRCLE.create(), event ->
		{
			DialogBookCreate.open(bookStoreDemo, book ->
			{
				bookStoreDemo.data().books().add(
					book,
					bookStoreDemo.storageManager()
				);
				this.updateDataProvider();
			});
		});

		this.add(
			new HorizontalLayout(createBookButton),
			this.grid
		);
		this.setSizeFull();

		this.updateDataProvider();
	}

	private void updateDataProvider()
	{
		this.dataProvider = DataProvider.ofCollection(
			this.bookStoreDemo.data().books().all()
		)
		.withConfigurableFilter();

		this.grid.setDataProvider(this.dataProvider);

		this.updateFilter();
	}

	private void updateFilter()
	{
		SerializablePredicate<Book> filter = book -> true;

		final String title = this.filterFieldTitle.getValue().trim();
		if(!title.isEmpty())
		{
			filter = filter.and(book -> StringUtils.containsIgnoreCase(book.title(), title));
		}

		final Author author = this.filterComboAutor.getValue();
		if(author != null)
		{
			filter = filter.and(book -> book.author() == author);
		}

		final Genre genre = this.filterComboGenre.getValue();
		if(genre != null)
		{
			filter = filter.and(book -> book.genre() == genre);
		}

		final Publisher publisher = this.filterComboPublisher.getValue();
		if(publisher != null)
		{
			filter = filter.and(book -> book.publisher() == publisher);
		}

		final String isbn = this.filterFieldIsbn.getValue().trim();
		if(!isbn.isEmpty())
		{
			filter = filter.and(book -> StringUtils.containsIgnoreCase(book.isbn13(), isbn));
		}

		this.dataProvider.setFilter(filter);
	}

}
