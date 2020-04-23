
package one.microstream.readmecorp.data;

public interface Book extends Entity
{
	public String isbn13();
	
	public String title();
	
	public Author author();
	
	public Genre genre();
	
	public Publisher publisher();
	
	public double price();
	
	public Language language();
	
	public static Book New(
		final String isbn13,
		final String title,
		final Author author,
		final Genre genre,
		final Publisher publisher,
		final Language language,
		final double price
	)
	{
		return new Default(isbn13, title, author, genre, publisher, language, price);
	}
	
	public static class Default implements Book
	{
		private final String    isbn13;
		private final String    title;
		private final Author    author;
		private final Genre     genre;
		private final Publisher publisher;
		private final Language  language;
		private final double    price;
		
		Default(
			final String isbn13,
			final String title,
			final Author author,
			final Genre genre,
			final Publisher publisher,
			final Language language,
			final double price
		)
		{
			super();
			this.isbn13    = isbn13;
			this.title     = title;
			this.author    = author;
			this.genre     = genre;
			this.publisher = publisher;
			this.language  = language;
			this.price     = price;
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
		public double price()
		{
			return this.price;
		}
		
		@Override
		public Language language()
		{
			return this.language;
		}

		@Override
		public String toString()
		{
			return "Book [isbn13=" + this.isbn13 + ", title=" + this.title + ", author=" + this.author + ", genre="
				+ this.genre + ", publisher=" + this.publisher + ", language=" + this.language + ", price=" + this.price
				+ "]";
		}
		
	}
	
}
