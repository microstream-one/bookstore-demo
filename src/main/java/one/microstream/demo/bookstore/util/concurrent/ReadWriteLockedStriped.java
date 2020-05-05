package one.microstream.demo.bookstore.util.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import com.google.common.util.concurrent.Striped;

/**
 * Facility to execute operations with striped read and write locks.
 * <p>
 * Conceptually, lock striping is the technique of dividing a lock into many stripes,
 * increasing the granularity of a single lock and allowing independent operations to lock
 * different stripes and proceed concurrently, instead of creating contention for a single lock.
 * <p>
 * Non-reentrant read operations are not allowed until all write operations of the affected stripe
 * have been finished.
 * Additionally, a write operation can acquire the read lock, but not vice-versa.
 */
public interface ReadWriteLockedStriped
{
	/**
	 * Executes an operation protected by a read lock for a given key.
	 *
	 * @param <T> the operation's return type
	 * @param key an arbitrary, non-null key
	 * @param operation the operation to execute
	 * @return the operation's result
	 */
	public <T> T read(Object key, ValueOperation<T> operation);

	/**
	 * Executes an operation protected by a read lock for a given key.
	 *
	 * @param key an arbitrary, non-null key
	 * @param operation the operation to execute
	 */
	public void read(Object key, VoidOperation operation);

	/**
	 * Executes an operation protected by a write lock for a given key.
	 *
	 * @param <T> the operation's return type
	 * @param key an arbitrary, non-null key
	 * @param operation the operation to execute
	 * @return the operation's result
	 */
	public <T> T write(Object key, ValueOperation<T> operation);

	/**
	 * Executes an operation protected by a write lock for a given key.
	 *
	 * @param key an arbitrary, non-null key
	 * @param operation the operation to execute
	 */
	public void write(Object key, VoidOperation operation);


	/**
	 * Pseudo-constructor method to create a new {@link ReadWriteLockedStriped}
	 * instance with default implementation.
	 *
	 * @return a new {@link ReadWriteLockedStriped} instance
	 */
	public static ReadWriteLockedStriped New()
	{
		return new Default();
	}


	/**
	 * Default implementation of the {@link ReadWriteLockedStriped} interface
	 * which utilizes {@link Striped} for locking.
	 *
	 */
	public static class Default implements ReadWriteLockedStriped
	{
		private final Striped<ReadWriteLock> stripes = Striped.lazyWeakReadWriteLock(4);

		Default()
		{
			super();
		}

		@Override
		public <T> T read(
			final Object key,
			final ValueOperation<T> operation
			)
		{
			final Lock readLock = this.stripes.get(key).readLock();
			readLock.lock();

			try
			{
				return operation.execute();
			}
			finally
			{
				readLock.unlock();
			}
		}

		@Override
		public void read(
			final Object key,
			final VoidOperation operation
		)
		{
			final Lock readLock = this.stripes.get(key).readLock();
			readLock.lock();

			try
			{
				operation.execute();
			}
			finally
			{
				readLock.unlock();
			}
		}

		@Override
		public <T> T write(
			final Object key,
			final ValueOperation<T> operation
		)
		{
			final Lock writeLock = this.stripes.get(key).writeLock();
			writeLock.lock();

			try
			{
				return operation.execute();
			}
			finally
			{
				writeLock.unlock();
			}
		}

		@Override
		public void write(
			final Object key,
			final VoidOperation operation
		)
		{
			final Lock writeLock = this.stripes.get(key).writeLock();
			writeLock.lock();

			try
			{
				operation.execute();
			}
			finally
			{
				writeLock.unlock();
			}
		}

	}


	/**
	 * Abstract base class for {@link ReadWriteLockedStriped} scopes.
	 *
	 */
	public static abstract class Scope implements ReadWriteLockedStriped
	{
		/*
		 * Transient means it is not persisted by MicroStream, but created on demand.
		 */
		private transient volatile ReadWriteLockedStriped delegate;

		protected Scope()
		{
			super();
		}

		protected ReadWriteLockedStriped delegate()
		{
			/*
			 * Double-checked locking to reduce the overhead of acquiring a lock
			 * by testing the locking criterion.
			 * The field (this.delegate) has to be volatile.
			 */
			ReadWriteLockedStriped delegate = this.delegate;
			if(delegate == null)
			{
				synchronized(this)
				{
					if((delegate = this.delegate) == null)
					{
						delegate = this.delegate = ReadWriteLockedStriped.New();
					}
				}
			}
			return delegate;
		}

		@Override
		public <T> T read(
			final Object key,
			final ValueOperation<T> operation
		)
		{
			return this.delegate().read(key, operation);
		}

		@Override
		public void read(
			final Object key,
			final VoidOperation operation
		)
		{
			this.delegate().read(key, operation);
		}

		@Override
		public <T> T write(
			final Object key,
			final ValueOperation<T> operation
		)
		{
			return this.delegate().write(key, operation);
		}

		@Override
		public void write(
			final Object key,
			final VoidOperation operation
		)
		{
			this.delegate().write(key, operation);
		}

	}

}
