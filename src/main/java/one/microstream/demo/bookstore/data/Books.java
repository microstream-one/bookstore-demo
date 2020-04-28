
package one.microstream.demo.bookstore.data;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.QueryBuilder;

import one.microstream.demo.bookstore.data.Index.DocumentPopulator;
import one.microstream.demo.bookstore.data.Index.EntityMatcher;
import one.microstream.demo.bookstore.util.HasMutex;


public interface Books
{
	public int bookCount();

	public <T> T compute(Function<Stream<Book>, T> streamFunction);

	public <T> T computeByAuthor(Author author, Function<Stream<Book>, T> streamFunction);

	public <T> T computeByGenre(Genre genre, Function<Stream<Book>, T> streamFunction);

	public <T> T computeByPublisher(Publisher publisher, Function<Stream<Book>, T> streamFunction);

	public <T> T computeByLanguage(Language language, Function<Stream<Book>, T> streamFunction);

	public Book ofIsbn13(String isbn13);

	public <T> T computeGenres(Function<Stream<Genre>, T> streamFunction);

	public <T> T computeAuthors(Function<Stream<Author>, T> streamFunction);

	public <T> T computePublishers(Function<Stream<Publisher>, T> streamFunction);

	public <T> T computeLanguages(Function<Stream<Language>, T> streamFunction);

	public List<Book> searchByTitle(String queryText);

	public default List<Book> allByAuthor(
		final Author author
	)
	{
		return this.computeByAuthor(
			author,
			books -> books.collect(toList())
		);
	}

	public default List<Book> allByGenre(
		final Genre genre
	)
	{
		return this.computeByGenre(
			genre,
			books -> books.collect(toList())
		);
	}

	public default List<Book> allByPublisher(
		final Publisher publisher
	)
	{
		return this.computeByPublisher(
			publisher,
			books -> books.collect(toList())
		);
	}

	public default List<Book> allByLanguage(
		final Language language
	)
	{
		return this.computeByLanguage(
			language,
			books -> books.collect(toList())
		);
	}

	public void add(Book book);

	public void addAll(Collection<? extends Book> books);

	public void clear();


	public static class Default extends HasMutex implements Books
	{
		private final Map<String, Book>          isbn13ToBook     = new HashMap<>(1024);
		private final Map<Author, List<Book>>    authorToBooks    = new HashMap<>(512);
		private final Map<Genre, List<Book>>     genreToBooks     = new HashMap<>(512);
		private final Map<Publisher, List<Book>> publisherToBooks = new HashMap<>(1024);
		private final Map<Language, List<Book>>  languageToBooks  = new HashMap<>(32);
		private transient Index<Book>            index;

		Default()
		{
			super();
		}

		@Override
		public void add(
			final Book book
		)
		{
			this.write(() ->
			{
				this.index().add(book);

				this.addToCollections(book);
			});
		}

		@Override
		public void addAll(
			final Collection<? extends Book> books
		)
		{
			this.write(() ->
			{
				this.index().addAll(books);

				books.forEach(this::addToCollections);
			});
		}

		private void addToCollections(
			final Book book
		)
		{
			this.isbn13ToBook.put(book.isbn13(), book);
			this.addToCollection(this.authorToBooks, book.author(), book);
			this.addToCollection(this.genreToBooks, book.genre(), book);
			this.addToCollection(this.publisherToBooks, book.publisher(), book);
			this.addToCollection(this.languageToBooks, book.language(), book);
		}

		private <K> void addToCollection(
			final Map<K, List<Book>> map,
			final K key,
			final Book book
		)
		{
			map.computeIfAbsent(
				key,
				k -> new ArrayList<>(1024)
			)
			.add(book);
		}

		@Override
		public void clear()
		{
			this.write(() ->
			{
				this.isbn13ToBook.clear();
				this.authorToBooks.clear();
				this.genreToBooks.clear();
				this.publisherToBooks.clear();
				this.languageToBooks.clear();

				this.index().clear();
			});
		}

		@Override
		public int bookCount()
		{
			return this.read(
				this.isbn13ToBook::size
			);
		}

		@Override
		public <T> T compute(
			final Function<Stream<Book>, T> streamFunction
		)
		{
			return this.read(() ->
				streamFunction.apply(this.isbn13ToBook.values().stream())
			);
		}

		@Override
		public <T> T computeByAuthor(
			final Author author,
			final Function<Stream<Book>, T> streamFunction
		)
		{
			return this.read(() ->
			{
				final List<Book> list = this.authorToBooks.get(author);
				return streamFunction.apply(
					list != null
						? list.stream()
						: Stream.empty()
				);
			});
		}

		@Override
		public  <T> T computeByGenre(
			final Genre genre,
			final Function<Stream<Book>, T> streamFunction
		)
		{
			return this.read(() ->
			{
				final List<Book> list = this.genreToBooks.get(genre);
				return streamFunction.apply(
					list != null
						? list.stream()
						: Stream.empty()
				);
			});
		}

		@Override
		public <T> T computeByPublisher(
			final Publisher publisher,
			final Function<Stream<Book>, T> streamFunction
		)
		{
			return this.read(() ->
			{
				final List<Book> list = this.publisherToBooks.get(publisher);
				return streamFunction.apply(
					list != null
						? list.stream()
						: Stream.empty()
				);
			});
		}

		@Override
		public <T> T computeByLanguage(
			final Language language,
			final Function<Stream<Book>, T> streamFunction
		)
		{
			return this.read(() ->
			{
				final List<Book> list = this.languageToBooks.get(language);
				return streamFunction.apply(
					list != null
						? list.stream()
						: Stream.empty()
				);
			});
		}

		@Override
		public Book ofIsbn13(
			final String isbn13
		)
		{
			return this.read(() ->
				this.isbn13ToBook.get(isbn13)
			);
		}

		@Override
		public <T> T computeGenres(
			final Function<Stream<Genre>, T> streamFunction
		)
		{
			return this.read(() ->
				streamFunction.apply(this.genreToBooks.keySet().stream())
			);
		}

		@Override
		public <T> T computeAuthors(
			final Function<Stream<Author>, T> streamFunction
		)
		{
			return this.read(() ->
				streamFunction.apply(this.authorToBooks.keySet().stream())
			);
		}

		@Override
		public <T> T computePublishers(
			final Function<Stream<Publisher>, T> streamFunction
		)
		{
			return this.read(() ->
				streamFunction.apply(this.publisherToBooks.keySet().stream())
			);
		}

		@Override
		public <T> T computeLanguages(
			final Function<Stream<Language>, T> streamFunction
		)
		{
			return this.read(() ->
				streamFunction.apply(this.languageToBooks.keySet().stream())
			);
		}

		@Override
		public List<Book> searchByTitle(
			final String queryText
		)
		{
			final Index<Book>  index        = this.index();
			final QueryBuilder queryBuilder = index.createQueryBuilder();
			final Query        query        = queryBuilder.createPhraseQuery("title", queryText);
			return index.search(query, Integer.MAX_VALUE);
		}

		private Index<Book> index()
		{
			if(this.index == null)
			{
				synchronized(this)
				{
					if(this.index == null)
					{
						this.index = this.createIndex();
					}
				}
			}
			return this.index;
		}

		private Index<Book> createIndex()
		{
			final DocumentPopulator<Book> documentPopulator = (document, book) -> {
				document.add(new StringField("isbn13", book.isbn13(), Store.YES));
				document.add(new TextField("title", book.title(), Store.YES));
				document.add(new TextField("author", book.author().name(), Store.YES));
				document.add(new TextField("genre", book.genre().name(), Store.YES));
				document.add(new TextField("publisher", book.publisher().name(), Store.YES));
			};

			final EntityMatcher<Book> entityMatcher = document ->
				this.isbn13ToBook.get(document.get("isbn13"))
			;

			final Index<Book> index = Index.New(
				Book.class,
				documentPopulator,
				entityMatcher
			);

			if(index.size() == 0 && this.bookCount() > 0)
			{
				index.addAll(this.isbn13ToBook.values());
			}

			return index;
		}

	}

}
