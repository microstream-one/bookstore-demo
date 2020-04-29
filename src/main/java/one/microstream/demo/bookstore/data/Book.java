
package one.microstream.demo.bookstore.data;

import static java.util.Objects.requireNonNull;
import static one.microstream.demo.bookstore.util.ValidationUtils.requireNonBlank;
import static one.microstream.demo.bookstore.util.ValidationUtils.requireNonEmpty;

import java.util.regex.Pattern;

import javax.money.MonetaryAmount;

public interface Book extends Named
{
	@Override
	default String name()
	{
		return this.title();
	}

	public String isbn13();

	public String title();

	public Author author();

	public Genre genre();

	public Publisher publisher();

	public Language language();

	public MonetaryAmount purchasePrice();

	public MonetaryAmount retailPrice();


	public static interface Validation
	{
		public static String isbn13Pattern()
		{
			return "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$";
		}

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

		public static String validateTitle(final String title)
		{
			return requireNonBlank(title, () -> "Title cannot be empty");
		}

		public static Author validateAuthor(final Author author)
		{
			return requireNonNull(author, () -> "Author cannot be null");
		}

		public static Genre validateGenre(final Genre genre)
		{
			return requireNonNull(genre, () -> "Genre cannot be null");
		}

		public static Publisher validatePublisher(final Publisher publisher)
		{
			return requireNonNull(publisher, () -> "Publisher cannot be null");
		}

		public static Language validateLanguage(final Language language)
		{
			return requireNonNull(language, () -> "Language cannot be null");
		}

		public static MonetaryAmount validatePrice(final MonetaryAmount price)
		{
			if(price.isNegativeOrZero())
			{
				throw new IllegalArgumentException("Price must be greater than 0");
			}
			return price;
		}
	}


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
