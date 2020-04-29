package one.microstream.demo.bookstore.ui.data;

import static java.util.stream.Collectors.toList;

import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.provider.DataProvider;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Author;
import one.microstream.demo.bookstore.data.Genre;
import one.microstream.demo.bookstore.data.Language;
import one.microstream.demo.bookstore.data.Publisher;

public interface DataBindingUtils
{
	public static void configureAuthorsComboBox(
		final BookStoreDemo bookStoreDemo,
		final ComboBox<Author> combo
	)
	{
		combo.setDataProvider(
			DataProvider.ofCollection(
				bookStoreDemo.data().books().computeAuthors(
					authors -> authors.sorted().collect(toList())
				)
			),
			filterText -> author -> StringUtils.containsIgnoreCase(author.name(), filterText)
		);
		combo.setItemLabelGenerator(Author::name);
	}

	public static void configureGenresComboBox(
		final BookStoreDemo bookStoreDemo,
		final ComboBox<Genre> combo
	)
	{
		combo.setDataProvider(
			DataProvider.ofCollection(
				bookStoreDemo.data().books().computeGenres(
					genres -> genres.sorted().collect(toList())
				)
			),
			filterText -> genre -> StringUtils.containsIgnoreCase(genre.name(), filterText)
		);
		combo.setItemLabelGenerator(Genre::name);
	}

	public static void configurePublishersComboBox(
		final BookStoreDemo bookStoreDemo,
		final ComboBox<Publisher> combo
	)
	{
		combo.setDataProvider(
			DataProvider.ofCollection(
				bookStoreDemo.data().books().computePublishers(
					publishers -> publishers.sorted().collect(toList())
				)
			),
			filterText -> publisher -> StringUtils.containsIgnoreCase(publisher.name(), filterText)
		);
		combo.setItemLabelGenerator(Publisher::name);
	}

	public static void configureLanguagesComboBox(
		final BookStoreDemo bookStoreDemo,
		final ComboBox<Language> combo
	)
	{
		combo.setDataProvider(
			DataProvider.ofCollection(
				bookStoreDemo.data().books().computeLanguages(
					languages -> languages.sorted().collect(toList())
				)
			),
			filterText -> language -> StringUtils.containsIgnoreCase(language.name(), filterText)
		);
		combo.setItemLabelGenerator(Language::name);
	}

	public static <T> Validator<? super T> validator(final Function<T, ?> validation)
	{
		return (value, context) ->
		{
			try
			{
				validation.apply(value);
				return ValidationResult.ok();
			}
			catch(final Exception e)
			{
				return ValidationResult.error(e.getMessage());
			}
		};
	}

	public static <B, V> Setter<B, V> setterDummy()
	{
		return (bean, fieldvalue) ->
		{
			// no-op
		};
	}
}
