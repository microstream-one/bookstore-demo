
package one.microstream.demo.bookstore.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.QueryBuilder;

import one.microstream.demo.bookstore.data.Index.DocumentPopulator;
import one.microstream.demo.bookstore.data.Index.EntityMatcher;


public interface Books
{
	public int size();

	public Stream<Book> all();

	public Stream<Book> allParallel();

	public Book ofIsbn13(String isbn13);

	public Stream<Book> byAuthor(
		Author author
	);

	public Stream<Book> byGenre(
		Genre genre
	);

	public Stream<Book> byPublisher(
		Publisher publisher
	);

	public Stream<Book> byLanguage(
		Language language
	);

	public Stream<Genre> genres();

	public Stream<Author> authors();

	public Stream<Publisher> publishers();

	public Stream<Language> languages();

	public Stream<Book> searchByTitle(
		String query
	);

	public static interface Mutable extends Books
	{
		public void add(
			Book book
		);

		public void addAll(
			Iterable<Book> books
		);

		public void clear();
	}

	public static class Default implements Books.Mutable
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
		public synchronized void add(
			final Book book
		)
		{
			this.index().add(book);

			this.addToCollections(book);
		}

		@Override
		public synchronized void addAll(
			final Iterable<Book> books
		)
		{
			this.index().addAll(books);

			books.forEach(this::addToCollections);
		}

		private void addToCollections(
			final Book book
		)
		{
			this.isbn13ToBook.put(book.isbn13(), book);
			this.add(this.authorToBooks, book.author(), book);
			this.add(this.genreToBooks, book.genre(), book);
			this.add(this.publisherToBooks, book.publisher(), book);
			this.add(this.languageToBooks, book.language(), book);
		}

		private <K> void add(
			final Map<K, List<Book>> map,
			final K key,
			final Book book
		)
		{
			map.computeIfAbsent(key, k -> new ArrayList<>(1024))
				.add(book);
		}

		@Override
		public synchronized void clear()
		{
			this.isbn13ToBook.clear();
			this.authorToBooks.clear();
			this.genreToBooks.clear();
			this.publisherToBooks.clear();
			this.languageToBooks.clear();

			this.index().clear();
		}

		@Override
		public int size()
		{
			return this.isbn13ToBook.size();
		}

		@Override
		public Stream<Book> all()
		{
			return this.isbn13ToBook.values().stream();
		}

		@Override
		public Stream<Book> allParallel()
		{
			return this.isbn13ToBook.values().parallelStream();
		}

		@Override
		public Book ofIsbn13(
			final String isbn13
		)
		{
			return this.isbn13ToBook.get(isbn13);
		}

		@Override
		public Stream<Book> byAuthor(
			final Author author
		)
		{
			final List<Book> list = this.authorToBooks.get(author);
			return list != null
				? list.stream()
				: Stream.empty();
		}

		@Override
		public Stream<Book> byGenre(
			final Genre genre
		)
		{
			final List<Book> list = this.genreToBooks.get(genre);
			return list != null
				? list.stream()
				: Stream.empty();
		}

		@Override
		public Stream<Book> byPublisher(
			final Publisher publisher
		)
		{
			final List<Book> list = this.publisherToBooks.get(publisher);
			return list != null
				? list.stream()
				: Stream.empty();
		}

		@Override
		public Stream<Book> byLanguage(
			final Language language
		)
		{
			final List<Book> list = this.languageToBooks.get(language);
			return list != null
				? list.stream()
				: Stream.empty();
		}

		@Override
		public Stream<Genre> genres()
		{
			return this.genreToBooks.keySet().stream();
		}

		@Override
		public Stream<Author> authors()
		{
			return this.authorToBooks.keySet().stream();
		}

		@Override
		public Stream<Publisher> publishers()
		{
			return this.publisherToBooks.keySet().stream();
		}

		@Override
		public Stream<Language> languages()
		{
			return this.languageToBooks.keySet().stream();
		}

		@Override
		public Stream<Book> searchByTitle(
			final String queryText
		)
		{
			final Index<Book>  index        = this.index();
			final QueryBuilder queryBuilder = index.createQueryBuilder();
			final Query        query        = queryBuilder.createPhraseQuery("title", queryText);
			return index.search(query, Integer.MAX_VALUE).stream();
		}

		private Index<Book> index()
		{
			if(this.index == null)
			{
				this.index = this.createIndex();
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

			if(index.size() == 0 && this.size() > 0)
			{
				index.addAll(this.isbn13ToBook.values());
			}

			return index;
		}

	}

}
