package one.microstream.demo.bookstore.util.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * Facility to execute operations with read and write locks.
 * <p>
 * Non-reentrant read operations are not allowed until all write operations have been finished.
 * Additionally, a write operation can acquire the read lock, but not vice-versa.
 */
public interface ReadWriteLocked
{
	/**
	 * Executes an operation protected by a read lock.
	 *
	 * @param <T> the operation's return type
	 * @param operation the operation to execute
	 * @return the operation's result
	 */
	public <T> T read(ValueOperation<T> operation);

	/**
	 * Executes an operation protected by a read lock.
	 *
	 * @param operation the operation to execute
	 */
	public void read(VoidOperation operation);

	/**
	 * Executes an operation protected by a write lock.
	 *
	 * @param <T> the operation's return type
	 * @param operation the operation to execute
	 * @return the operation's result
	 */
	public <T> T write(ValueOperation<T> operation);

	/**
	 * Executes an operation protected by a write lock.
	 *
	 * @param operation the operation to execute
	 */
	public void write(VoidOperation operation);


	/**
	 * Pseudo-constructor method to create a new {@link ReadWriteLocked}
	 * instance with default implementation.
	 *
	 * @return a new {@link ReadWriteLocked} instance
	 */
	public static ReadWriteLocked New()
	{
		return new Default();
	}


	/**
	 * Default implementation of the {@link ReadWriteLocked} interface
	 * which utilizes a {@link ReentrantReadWriteLock} for locking.
	 *
	 */
	public static class Default implements ReadWriteLocked
	{
		private final ReentrantReadWriteLock mutex = new ReentrantReadWriteLock();

		Default()
		{
			super();
		}

		@Override
		public <T> T read(final ValueOperation<T> operation)
		{
			final ReadLock readLock = this.mutex.readLock();
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
		public void read(final VoidOperation operation)
		{
			final ReadLock readLock = this.mutex.readLock();
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
		public <T> T write(final ValueOperation<T> operation)
		{
			final WriteLock writeLock = this.mutex.writeLock();
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
		public void write(final VoidOperation operation)
		{
			final WriteLock writeLock = this.mutex.writeLock();
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
	 * Abstract base class for {@link ReadWriteLocked} scopes.
	 *
	 */
	public static abstract class Scope implements ReadWriteLocked
	{
		/*
		 * Transient means it is not persisted by MicroStream, but created on demand.
		 */
		private transient volatile ReadWriteLocked delegate;

		protected Scope()
		{
			super();
		}

		protected ReadWriteLocked delegate()
		{
			/*
			 * Double-checked locking to reduce the overhead of acquiring a lock
			 * by testing the locking criterion.
			 * The field (this.delegate) has to be volatile.
			 */
			ReadWriteLocked delegate = this.delegate;
			if(delegate == null)
			{
				synchronized(this)
				{
					if((delegate = this.delegate) == null)
					{
						delegate = this.delegate = ReadWriteLocked.New();
					}
				}
			}
			return delegate;
		}

		@Override
		public <T> T read(final ValueOperation<T> operation)
		{
			return this.delegate().read(operation);
		}

		@Override
		public void read(final VoidOperation operation)
		{
			this.delegate().read(operation);
		}

		@Override
		public <T> T write(final ValueOperation<T> operation)
		{
			return this.delegate().write(operation);
		}

		@Override
		public void write(final VoidOperation operation)
		{
			this.delegate().write(operation);
		}

	}

}
