
package one.microstream.readmecorp.data;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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


public interface Index<T> extends Closeable
{
	public static interface DocumentPopulator<T> extends BiConsumer<Document, T>
	{
	}

	public static interface EntityMatcher<T> extends Function<Document, T>
	{
	}

	public void add(
		T entity
	);

	public void addAll(
		Iterable<T> entities
	);

	public void remove(
		Query query
	);

	public void clear();

	public List<T> search(
		Query query,
		int maxResults
	);

	public QueryBuilder createQueryBuilder();

	public int size();

	public static <T> Index<T> New(
		final Class<T> entityType,
		final DocumentPopulator<T> documentPopulator,
		final EntityMatcher<T> entityMatcher
	)
	{
		return new Default<>(
			entityType,
			documentPopulator,
			entityMatcher
		);
	}

	public static class Default<T> implements Index<T>
	{
		private final Class<T>             entityType;
		private final DocumentPopulator<T> documentPopulator;
		private final EntityMatcher<T>     entityMatcher;
		private MMapDirectory              directory;
		private IndexWriter                writer;
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
			final Iterable<T> entities
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
		public void clear()
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
			if(this.directory == null)
			{
				final Path path = Paths.get(
					"data",
					"index",
					this.entityType.getSimpleName()
				);
				try
				{
					this.directory = new MMapDirectory(path);
					this.writer = new IndexWriter(
						this.directory,
						new IndexWriterConfig(new StandardAnalyzer())
					);
					this.searcher = new IndexSearcher(
						DirectoryReader.open(this.writer)
					);
				}
				catch(final IOException e)
				{
					throw new IORuntimeException(e);
				}
			}
		}

		@Override
		public void close() throws IOException
		{
			if(this.directory != null)
			{
				this.writer.close();
				this.directory.close();

				this.directory = null;
				this.writer    = null;
				this.searcher  = null;
			}
		}

	}

}
