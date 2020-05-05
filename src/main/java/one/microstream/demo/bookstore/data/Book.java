
package one.microstream.demo.bookstore.data;

import static java.util.Objects.requireNonNull;
import static one.microstream.demo.bookstore.util.ValidationUtils.requireNonBlank;
import static one.microstream.demo.bookstore.util.ValidationUtils.requireNonEmpty;

import java.util.regex.Pattern;

import javax.money.MonetaryAmount;

/**
 * Book entity which holds an ISBN-13, title, {@link Author}, {@link Genre}, {@link Publisher},
 * {@link Language} and the purchase and retail price.
 * <p>
 * This type is immutable and therefor inherently thread safe.
 *
 */
public interface Book extends Named
{
	/**
	 * Gets the title of this book. This is a synonym method to get compatibility to the {@link Named} interface.
	 */
	@Override
	default String name()
	{
		return this.title();
	}

	/**
	 * Get the ISBN-13.
	 *
	 * @return the ISBN-13
	 */
	public String isbn13();

	/**
	 * Get the title.
	 *
	 * @return the title
	 */
	public String title();

	/**
	 * Get the author.
	 *
	 * @return the author
	 */
	public Author author();

	/**
	 * Get the genre.
	 *
	 * @return the genre
	 */
	public Genre genre();

	/**
	 * Get the publisher.
	 *
	 * @return the publisher
	 */
	public Publisher publisher();

	/**
	 * Get the language.
	 *
	 * @return the language
	 */
	public Language language();

	/**
	 * Get the purchase price.
	 *
	 * @return the purchase price
	 */
	public MonetaryAmount purchasePrice();

	/**
	 * Get the retail price.
	 *
	 * @return the retail price
	 */
	public MonetaryAmount retailPrice();


	/**
	 * Validation utilities for the {@link Book} entity.
	 *
	 */
	public static interface Validation
	{
		/**
		 * @return Regular expression for a valid ISBN-13
		 */
		public static String isbn13Pattern()
		{
			return "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$";
		}

		/**
		 * Validates the given ISBN.
		 * It is returned if valid, otherwise an exception is thrown.
		 *
		 * @param isbn13 ths ISBN to validate
		 * @return the given ISBN
		 * @throws IllegalArgumentException if the given ISBN is invalid
		 * @throws NumberFormatException if parts of the ISBN are no numbers
		 */
		public static String validateIsbn13(final String isbn13)
	    {
			requireNonEmpty(isbn13, () -> "ISBN cannot be empty");

			if(!Pattern.matches(isbn13Pattern(), isbn13))
			{
				throw new IllegalArgumentException("Invalid ISBN format");
			}

			final String isbn13withoutSeparators = isbn13.replace("-", "");

			int total = 0;
			for(int i = 0; i < 12; i++)
			{
				final int digit = Integer.parseInt(isbn13withoutSeparators.substring(i, i + 1));
				total += i % 2 == 0
					? digit
					: digit * 3;
			}

			int checksum = 10 - total % 10;
			if(checksum == 10)
			{
				checksum = 0;
			}

			if(checksum != Integer.parseInt(isbn13withoutSeparators.substring(12)))
			{
				throw new IllegalArgumentException("Invalid ISBN checksum");
			}

			return isbn13;
	    }

		/**
		 * Validates the given title.
		 * It is returned if not empty or <code>null</code>, otherwise an exception is thrown.
		 *
		 * @param title the title to validate
		 * @return the given title
		 * @throws IllegalArgumentException if the given title is empty or <code>null</code>
		 */
		public static String validateTitle(final String title)
		{
			return requireNonBlank(title, () -> "Title cannot be empty");
		}

		/**
		 * Validates the given author.
		 * It is returned if not <code>null</code>, otherwise an exception is thrown.
		 *
		 * @param author the author to validate
		 * @return the given author
		 * @throws NullPointerException if the given author is <code>null</code>
		 */
		public static Author validateAuthor(final Author author)
		{
			return requireNonNull(author, () -> "Author cannot be null");
		}

		/**
		 * Validates the given genre.
		 * It is returned if not <code>null</code>, otherwise an exception is thrown.
		 *
		 * @param genre the genre to validate
		 * @return the given genre
		 * @throws NullPointerException if the given genre is <code>null</code>
		 */
		public static Genre validateGenre(final Genre genre)
		{
			return requireNonNull(genre, () -> "Genre cannot be null");
		}

		/**
		 * Validates the given publisher.
		 * It is returned if not <code>null</code>, otherwise an exception is thrown.
		 *
		 * @param publisher the publisher to validate
		 * @return the given publisher
		 * @throws NullPointerException if the given publisher is <code>null</code>
		 */
		public static Publisher validatePublisher(final Publisher publisher)
		{
			return requireNonNull(publisher, () -> "Publisher cannot be null");
		}

		/**
		 * Validates the given language.
		 * It is returned if not <code>null</code>, otherwise an exception is thrown.
		 *
		 * @param language the language to validate
		 * @return the given language
		 * @throws NullPointerException if the given language is <code>null</code>
		 */
		public static Language validateLanguage(final Language language)
		{
			return requireNonNull(language, () -> "Language cannot be null");
		}

		/**
		 * Validates the given price.
		 * It is returned if bigger then zero, otherwise an exception is thrown.
		 *
		 * @param price the price to validate
		 * @return the given price
		 * @throws IllegalArgumentException if the given price is negative or zero.
		 */
		public static MonetaryAmount validatePrice(final MonetaryAmount price)
		{
			if(price.isNegativeOrZero())
			{
				throw new IllegalArgumentException("Price must be greater than 0");
			}
			return price;
		}
	}


	/**
	 * Pseudo-constructor method to create a new {@link Book} instance with default implementation.
	 *
	 * @param isbn13 a valid ISBN-13, see {@link Validation#validateIsbn13(String)}
	 * @param title not empty, see {@link Validation#validateTitle(String)}
	 * @param author not <code>null</code>, see {@link Validation#validateAuthor(Author)}
	 * @param genre not <code>null</code>, see {@link Validation#validateGenre(Genre)}
	 * @param publisher not <code>null</code>, see {@link Validation#validatePublisher(Publisher)}
	 * @param language not <code>null</code>, see {@link Validation#validateLanguage(Language)}
	 * @param purchasePrice valid price, see {@link Validation#validatePrice(MonetaryAmount)}
	 * @param retailPrice valid price, see {@link Validation#validatePrice(MonetaryAmount)}
	 * @return
	 */
	public static Book New(
		final String isbn13,
		final String title,
		final Author author,
		final Genre genre,
		final Publisher publisher,
		final Language language,
		final MonetaryAmount purchasePrice,
		final MonetaryAmount retailPrice
	)
	{
		return new Default(
			Validation.validateIsbn13(isbn13),
			Validation.validateTitle(title),
			Validation.validateAuthor(author),
			Validation.validateGenre(genre),
			Validation.validatePublisher(publisher),
			Validation.validateLanguage(language),
			Validation.validatePrice(purchasePrice),
			Validation.validatePrice(retailPrice)
		);
	}


	/**
	 * Default implementation of the {@link Book} interface.
	 *
	 */
	public static class Default implements Book
	{
		private final String         isbn13;
		private final String         title;
		private final Author         author;
		private final Genre          genre;
		private final Publisher      publisher;
		private final Language       language;
		private final MonetaryAmount purchasePrice;
		private final MonetaryAmount retailPrice;

		Default(
			final String isbn13,
			final String title,
			final Author author,
			final Genre genre,
			final Publisher publisher,
			final Language language,
			final MonetaryAmount purchasePrice,
			final MonetaryAmount retailPrice
		)
		{
			super();
			this.isbn13        = isbn13;
			this.title         = title;
			this.author        = author;
			this.genre         = genre;
			this.publisher     = publisher;
			this.language      = language;
			this.purchasePrice = purchasePrice;
			this.retailPrice   = retailPrice;
		}

		@Override
		public String isbn13()
		{
			return this.isbn13;
		}

		@Override
		public String title()
		{
			return this.title;
		}

		@Override
		public Author author()
		{
			return this.author;
		}

		@Override
		public Genre genre()
		{
			return this.genre;
		}

		@Override
		public Publisher publisher()
		{
			return this.publisher;
		}

		@Override
		public Language language()
		{
			return this.language;
		}

		@Override
		public MonetaryAmount purchasePrice()
		{
			return this.purchasePrice;
		}

		@Override
		public MonetaryAmount retailPrice()
		{
			return this.retailPrice;
		}

		@Override
		public String toString()
		{
			return "Book [isbn13="
				+ this.isbn13
				+ ", title="
				+ this.title
				+ ", author="
				+ this.author
				+ ", genre="
				+ this.genre
				+ ", publisher="
				+ this.publisher
				+ ", language="
				+ this.language
				+ ", purchasePrice="
				+ this.purchasePrice
				+ ", retailPrice="
				+ this.retailPrice
				+ "]";
		}

	}

}
