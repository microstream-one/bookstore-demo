
package one.microstream.demo.bookstore.data;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.QueryBuilder;

import one.microstream.exceptions.IORuntimeException;

/**
 * Lucene based full text search index for Java objects.
 * <p>
 * All operations on this type are thread safe.
 *
 * @param <T> the object type
 * @see Books.Default#searchByTitle(String)
 */
public interface Index<T> extends Closeable
{
	/**
	 * External handler to populate index documents based on Java objects
	 *
	 * @param <T> the object type
	 */
	public static interface DocumentPopulator<T> extends BiConsumer<Document, T>
	{
	}

	/**
	 * External handler which matches Java objects to index documents.
	 *
	 * @param <T> the object type
	 */
	public static interface EntityMatcher<T> extends Function<Document, T>
	{
	}

	/**
	 * Adds an Java object to this index
	 *
	 * @param entity the java object to add
	 */
	public void add(T entity);

	/**
	 * Adds Java objects to this index, in a bulk operation.
	 *
	 * @param entities the java objects to add
	 */
	public void addAll(Collection<? extends T> entities);

	/**
	 * Removes documents from this index based on a query.
	 *
	 * @param query the query to match the documents which should be removed
	 */
	public void remove(Query query);

	/**
	 * Removes all entries from this index.
	 */
	public void clear();

	/**
	 * Queries this index.
	 *
	 * @param query the search query
	 * @param maxResults maximum number of results
	 * @return the list of found objects
	 */
	public List<T> search(Query query, int maxResults);

	/**
	 * Creates a new Lucene query builder.
	 *
	 * @return a new query builder.
	 */
	public QueryBuilder createQueryBuilder();

	/**
	 * Get the amount of entries in this index.
	 *
	 * @return amount of entries
	 */
	public int size();


	/**
	 * Pseudo-constructor method to create a new {@link Index} instance with default implementation.
	 *
	 * @param <T> the object type
	 * @param entityType not <code>null</code>
	 * @param documentPopulator not <code>null</code>
	 * @param entityMatcher not <code>null</code>
	 * @return a new {@link Index} instance
	 */
	public static <T> Index<T> New(
		final Class<T> entityType,
		final DocumentPopulator<T> documentPopulator,
		final EntityMatcher<T> entityMatcher
	)
	{
		return new Default<>(
			Objects.requireNonNull(entityType, () -> "EntityType cannot be null"),
			Objects.requireNonNull(documentPopulator, () -> "DocumentPopulator cannot be null"),
			Objects.requireNonNull(entityMatcher, () -> "EntityMatcher cannot be null")
		);
	}


	/**
	 * Default implementation of the {@link Index} interface.
	 *
	 */
	public static class Default<T> implements Index<T>
	{
		private final Class<T>             entityType;
		private final DocumentPopulator<T> documentPopulator;
		private final EntityMatcher<T>     entityMatcher;
		private MMapDirectory              directory;
		private IndexWriter                writer;
		private DirectoryReader            reader;
		private IndexSearcher              searcher;

		Default(
			final Class<T> entityType,
			final DocumentPopulator<T> documentPopulator,
			final EntityMatcher<T> entityMatcher
		)
		{
			super();
			this.entityType        = entityType;
			this.documentPopulator = documentPopulator;
			this.entityMatcher     = entityMatcher;
		}

		@Override
		public synchronized void add(
			final T entity
		)
		{
			this.lazyInit();

			try
			{
				final Document document = new Document();
				this.documentPopulator.accept(document, entity);
				this.writer.addDocument(document);
				this.writer.flush();
				this.writer.commit();
			}
			catch(final IOException e)
			{
				throw new IORuntimeException(e);
			}
		}

		@Override
		public synchronized void addAll(
			final Collection<? extends T> entities
		)
		{
			this.lazyInit();

			try
			{
				for(final T entity : entities)
				{
					final Document document = new Document();
					this.documentPopulator.accept(document, entity);
					this.writer.addDocument(document);
				}

				this.writer.flush();
				this.writer.commit();
			}
			catch(final IOException e)
			{
				throw new IORuntimeException(e);
			}
		}

		@Override
		public synchronized void remove(
			final Query query
		)
		{
			this.lazyInit();

			try
			{
				this.writer.deleteDocuments(query);
				this.writer.flush();
				this.writer.commit();
			}
			catch(final IOException e)
			{
				throw new IORuntimeException(e);
			}
		}

		@Override
		public synchronized void clear()
		{
			this.lazyInit();

			try
			{
				this.writer.deleteAll();
				this.writer.flush();
				this.writer.commit();
			}
			catch(final IOException e)
			{
				throw new IORuntimeException(e);
			}
		}

		@Override
		public synchronized List<T> search(
			final Query query,
			final int maxResults
		)
		{
			this.lazyInit();

			try
			{
				final TopDocs topDocs = this.searcher.search(query, maxResults);
				final List<T> result = new ArrayList<>(topDocs.scoreDocs.length);
				for(final ScoreDoc scoreDoc : topDocs.scoreDocs)
				{
					final Document document = this.searcher.doc(scoreDoc.doc);
					final T entity = this.entityMatcher.apply(document);
					if(entity != null)
					{
						result.add(entity);
					}
				}
				return result;
			}
			catch(final IOException e)
			{
				throw new IORuntimeException(e);
			}
		}

		@Override
		public synchronized QueryBuilder createQueryBuilder()
		{
			this.lazyInit();

			return new QueryBuilder(
				this.writer.getAnalyzer()
			);
		}

		@Override
		public synchronized int size()
		{
			this.lazyInit();

			return this.searcher.getIndexReader().numDocs();
		}

		private void lazyInit()
		{
			try
			{
				if(this.directory == null)
				{
					final Path path = Paths.get(
						"data",
						"index",
						this.entityType.getSimpleName()
					);
					this.directory = new MMapDirectory(path);
					this.writer = new IndexWriter(
						this.directory,
						new IndexWriterConfig(new StandardAnalyzer())
					);
					this.searcher = new IndexSearcher(
						this.reader = DirectoryReader.open(this.writer)
					);
				}
				else
				{
					final DirectoryReader newReader = DirectoryReader.openIfChanged(this.reader);
					if(newReader != null && newReader != this.reader)
					{
						this.reader.close();
						this.reader   = newReader;
						this.searcher = new IndexSearcher(this.reader);
					}
				}
			}
			catch(final IOException e)
			{
				throw new IORuntimeException(e);
			}
		}

		@Override
		public synchronized void close() throws IOException
		{
			if(this.directory != null)
			{
				this.writer.close();
				this.reader.close();
				this.directory.close();

				this.directory = null;
				this.writer    = null;
				this.reader    = null;
				this.searcher  = null;
			}
		}

	}

}
