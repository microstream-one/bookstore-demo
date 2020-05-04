package one.microstream.demo.bookstore.ui.data;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.data.provider.AbstractDataProvider;
import com.vaadin.flow.data.provider.InMemoryDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.function.SerializableComparator;
import com.vaadin.flow.function.SerializablePredicate;

public interface BookStoreDataProvider<T> extends InMemoryDataProvider<T>
{
	@FunctionalInterface
	public static interface Backend<T>
	{
		public <R> R compute(Function<Stream<T>, R> function);
	}


	public static <T> BookStoreDataProvider<T> New(
		final Backend<T> backend
	)
	{
		return new Default<>(backend);
	}


	public static class Default<T>
		extends AbstractDataProvider<T, SerializablePredicate<T>>
		implements BookStoreDataProvider<T>
	{
		private final Backend<T>          backend;
		private SerializableComparator<T> sortOrder;
		private SerializablePredicate<T>  filter;

		Default(
			final Backend<T> backend
		)
		{
			super();
			this.backend = backend;
		}

		@Override
		public int size(
			final Query<T, SerializablePredicate<T>> query
		)
		{
			return this.backend.compute(stream ->
				(int)this.getFilteredStream(stream, query).count()
			);
		}

		@Override
		public Stream<T> fetch(
			final Query<T, SerializablePredicate<T>> query
		)
		{
			return this.backend.compute(stream ->
			{
				stream = this.getFilteredStream(stream, query);
				final Optional<Comparator<T>> comparing = Stream
	                .of(query.getInMemorySorting(), this.sortOrder)
	                .filter(Objects::nonNull)
	                .reduce((c1, c2) -> c1.thenComparing(c2));
			    if(comparing.isPresent())
			    {
			        stream = stream.sorted(comparing.get());
			    }
			    return stream
			    	.skip(query.getOffset())
			    	.limit(query.getLimit())
			    	.collect(Collectors.toList());
			})
			.stream();
		}

	    private Stream<T> getFilteredStream(
			final Stream<T> stream,
			final Query<T, SerializablePredicate<T>> query
	    )
	    {
	    	final Stream<T> filteredStream = this.filter != null
	    		? stream.filter(this.filter)
	    		: stream;
	        return query.getFilter()
	        	.map(filteredStream::filter)
	        	.orElse(filteredStream);
	    }

		@Override
		public SerializablePredicate<T> getFilter()
		{
			return this.filter;
		}

		@Override
		public void setFilter(final SerializablePredicate<T> filter)
		{
			this.filter = filter;
			this.refreshAll();
		}

		@Override
		public SerializableComparator<T> getSortComparator()
		{
			return this.sortOrder;
		}

		@Override
		public void setSortComparator(final SerializableComparator<T> sortOrder)
		{
			this.sortOrder = sortOrder;
			this.refreshAll();
		}

	}

}
