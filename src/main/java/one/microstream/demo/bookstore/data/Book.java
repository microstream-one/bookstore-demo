
package one.microstream.demo.bookstore.data;

import javax.money.MonetaryAmount;

public interface Book extends Entity
{
	public String isbn13();

	public String title();

	public Author author();

	public Genre genre();

	public Publisher publisher();

	public MonetaryAmount purchasePrice();

	public MonetaryAmount retailPrice();

	public Language language();


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
		return new Default(isbn13, title, author, genre, publisher, language, purchasePrice, retailPrice);
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
		public Language language()
		{
			return this.language;
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
