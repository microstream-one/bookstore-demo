package one.microstream.demo.bookstore.ui.views;

import static one.microstream.demo.bookstore.ui.data.DataBindingUtils.setterDummy;
import static one.microstream.demo.bookstore.ui.data.DataBindingUtils.validator;

import java.util.function.Consumer;

import javax.money.MonetaryAmount;

import com.github.javafaker.Faker;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Author;
import one.microstream.demo.bookstore.data.Book;
import one.microstream.demo.bookstore.data.Genre;
import one.microstream.demo.bookstore.data.Language;
import one.microstream.demo.bookstore.data.Publisher;
import one.microstream.demo.bookstore.ui.data.DataBindingUtils;
import one.microstream.demo.bookstore.ui.data.DoubleToMonetaryAmountConverter;

@SuppressWarnings("serial")
public class DialogBookCreate extends Dialog
{
	public static void open(
		final BookStoreDemo bookStoreDemo,
		final Consumer<Book> successHandler
	)
	{
		new DialogBookCreate(bookStoreDemo, successHandler).open();
	}


	private final BookStoreDemo bookStoreDemo;

	private DialogBookCreate(
		final BookStoreDemo bookStoreDemo,
		final Consumer<Book> successHandler
	)
	{
		super();

		this.bookStoreDemo = bookStoreDemo;

		final TextField isbn13Field = new TextField("ISBN-13");
		isbn13Field.setPattern(Book.Validation.isbn13Pattern());
		isbn13Field.setValue(this.generateIsbn13());

		final TextField titleField = new TextField("Title");

		final ComboBox<Author> authorCombo = new ComboBox<>("Author");
		DataBindingUtils.configureAuthorsComboBox(bookStoreDemo, authorCombo);

		final ComboBox<Genre> genreCombo = new ComboBox<>("Genre");
		DataBindingUtils.configureGenresComboBox(bookStoreDemo, genreCombo);

		final ComboBox<Publisher> publisherCombo = new ComboBox<>("Publisher");
		DataBindingUtils.configurePublishersComboBox(bookStoreDemo, publisherCombo);

		final ComboBox<Language> languageCombo = new ComboBox<>("Language");
		DataBindingUtils.configureLanguagesComboBox(bookStoreDemo, languageCombo);

		final NumberField purchasePriceField = new NumberField("Purchase Price");

		final Binder<Book> binder = new Binder<>();
		binder.forField(isbn13Field)
			.asRequired()
			.withValidator(validator(this::validateIsbn13))
			.bind(Book::isbn13, setterDummy());
		binder.forField(titleField)
			.asRequired()
			.withValidator(validator(Book.Validation::validateTitle))
			.bind(Book::title, setterDummy());
		binder.forField(authorCombo)
			.asRequired()
			.withValidator(validator(Book.Validation::validateAuthor))
			.bind(Book::author, setterDummy());
		binder.forField(genreCombo)
			.asRequired()
			.withValidator(validator(Book.Validation::validateGenre))
			.bind(Book::genre, setterDummy());
		binder.forField(publisherCombo)
			.asRequired()
			.withValidator(validator(Book.Validation::validatePublisher))
			.bind(Book::publisher, setterDummy());
		binder.forField(languageCombo)
			.asRequired()
			.withValidator(validator(Book.Validation::validateLanguage))
			.bind(Book::language, setterDummy());
		binder.forField(purchasePriceField)
			.asRequired()
			.withConverter(new DoubleToMonetaryAmountConverter())
			.withValidator(validator(Book.Validation::validatePrice))
			.bind(Book::purchasePrice, setterDummy());

		final FormLayout form = new FormLayout(
			isbn13Field,
			titleField,
			authorCombo,
			genreCombo,
			publisherCombo,
			languageCombo,
			purchasePriceField
		);

		final Button okButton = new Button("OK", event ->
		{
			if(binder.validate().isOk())
			{
				final MonetaryAmount purchasePrice = BookStoreDemo.money(purchasePriceField.getValue());
				final Book book = Book.New(
					isbn13Field.getValue(),
					titleField.getValue(),
					authorCombo.getValue(),
					genreCombo.getValue(),
					publisherCombo.getValue(),
					languageCombo.getValue(),
					purchasePrice,
					BookStoreDemo.retailPrice(purchasePrice)
				);
				this.close();
				successHandler.accept(book);
			}
		});
		okButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		final Button cancelButton = new Button("Cancel", event -> this.close());
		cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
		this.add(new VerticalLayout(
			new H3("Create New Book"),
			form,
			new HorizontalLayout(okButton, cancelButton)
		));
	}

	private String generateIsbn13()
	{
		final Faker faker = Faker.instance();
		String isbn;
		while(this.bookStoreDemo.data().books().ofIsbn13(isbn = faker.code().isbn13(true)) != null)
		{
			; // empty loop
		}
		return isbn;
	}

	private String validateIsbn13(final String isbn13)
	{
		Book.Validation.validateIsbn13(isbn13);
		if(this.bookStoreDemo.data().books().ofIsbn13(isbn13) != null)
		{
			throw new IllegalArgumentException("ISBN already assigned to another book");
		}
		return isbn13;
	}
}
