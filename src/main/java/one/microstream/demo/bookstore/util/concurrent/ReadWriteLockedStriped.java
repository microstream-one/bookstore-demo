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
public class ReadWriteLockedStriped
{
	/*
	 * Transient means it is not persisted by MicroStream, but created on demand.
	 */
	private transient volatile Striped<ReadWriteLock> stripes;

	public ReadWriteLockedStriped()
	{
		super();
	}
	
	private Striped<ReadWriteLock> stripes()
	{
		/*
		 * Double-checked locking to reduce the overhead of acquiring a lock
		 * by testing the locking criterion.
		 * The field (this.delegate) has to be volatile.
		 */
		Striped<ReadWriteLock> stripes = this.stripes;
		if(stripes == null)
		{
			synchronized(this)
			{
				if((stripes = this.stripes) == null)
				{
					stripes = this.stripes = Striped.lazyWeakReadWriteLock(4);
				}
			}
		}
		return stripes;
	}
	
	/**
	 * Executes an operation protected by a read lock for a given key.
	 *
	 * @param <T> the operation's return type
	 * @param key an arbitrary, non-null key
	 * @param operation the operation to execute
	 * @return the operation's result
	 */
	public final <T> T read(
		final Object            key      ,
		final ValueOperation<T> operation
		)
	{
		final Lock readLock = this.stripes().get(key).readLock();
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
	 * Executes an operation protected by a read lock for a given key.
	 *
	 * @param key an arbitrary, non-null key
	 * @param operation the operation to execute
	 */
	public final void read(
		final Object        key      ,
		final VoidOperation operation
	)
	{
		final Lock readLock = this.stripes().get(key).readLock();
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
	 * Executes an operation protected by a write lock for a given key.
	 *
	 * @param <T> the operation's return type
	 * @param key an arbitrary, non-null key
	 * @param operation the operation to execute
	 * @return the operation's result
	 */
	public final <T> T write(
		final Object            key      ,
		final ValueOperation<T> operation
	)
	{
		final Lock writeLock = this.stripes().get(key).writeLock();
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
	 * Executes an operation protected by a write lock for a given key.
	 *
	 * @param key an arbitrary, non-null key
	 * @param operation the operation to execute
	 */
	public final void write(
		final Object        key      ,
		final VoidOperation operation
	)
	{
		final Lock writeLock = this.stripes().get(key).writeLock();
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
