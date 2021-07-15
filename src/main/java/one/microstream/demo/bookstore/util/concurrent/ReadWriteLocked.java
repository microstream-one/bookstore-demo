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
public class ReadWriteLocked
{
	/*
	 * Transient means it is not persisted by MicroStream, but created on demand.
	 */
	private transient volatile ReentrantReadWriteLock mutex;

	public ReadWriteLocked()
	{
		super();
	}
	
	private ReentrantReadWriteLock mutex()
	{
		/*
		 * Double-checked locking to reduce the overhead of acquiring a lock
		 * by testing the locking criterion.
		 * The field (this.mutex) has to be volatile.
		 */
		ReentrantReadWriteLock mutex = this.mutex;
		if(mutex == null)
		{
			synchronized(this)
			{
				if((mutex = this.mutex) == null)
				{
					mutex = this.mutex = new ReentrantReadWriteLock();
				}
			}
		}
		return mutex;
	}
	
	/**
	 * Executes an operation protected by a read lock.
	 *
	 * @param <T> the operation's return type
	 * @param operation the operation to execute
	 * @return the operation's result
	 */
	public final <T> T read(final ValueOperation<T> operation)
	{
		final ReadLock readLock = this.mutex().readLock();
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

	/**
	 * Executes an operation protected by a read lock.
	 *
	 * @param operation the operation to execute
	 */
	public final void read(final VoidOperation operation)
	{
		final ReadLock readLock = this.mutex().readLock();
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

	/**
	 * Executes an operation protected by a write lock.
	 *
	 * @param <T> the operation's return type
	 * @param operation the operation to execute
	 * @return the operation's result
	 */
	public final <T> T write(final ValueOperation<T> operation)
	{
		final WriteLock writeLock = this.mutex().writeLock();
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

	/**
	 * Executes an operation protected by a write lock.
	 *
	 * @param operation the operation to execute
	 */
	public final void write(final VoidOperation operation)
	{
		final WriteLock writeLock = this.mutex().writeLock();
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
