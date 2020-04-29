package one.microstream.demo.bookstore.util.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import com.google.common.util.concurrent.Striped;

public interface ReadWriteLockedStriped
{
	public <T> T read(Object key, ValueOperation<T> op);

	public void read(Object key, VoidOperation op);

	public <T> T write(Object key, ValueOperation<T> op);

	public void write(Object key, VoidOperation op);


	public static ReadWriteLockedStriped New()
	{
		return new Default();
	}


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
			final ValueOperation<T> op
			)
		{
			final Lock readLock = this.stripes.get(key).readLock();
			readLock.lock();

			try
			{
				return op.execute();
			}
			finally
			{
				readLock.unlock();
			}
		}

		@Override
		public void read(
			final Object key,
			final VoidOperation op
		)
		{
			final Lock readLock = this.stripes.get(key).readLock();
			readLock.lock();

			try
			{
				op.execute();
			}
			finally
			{
				readLock.unlock();
			}
		}

		@Override
		public <T> T write(
			final Object key,
			final ValueOperation<T> op
		)
		{
			final Lock writeLock = this.stripes.get(key).writeLock();
			writeLock.lock();

			try
			{
				return op.execute();
			}
			finally
			{
				writeLock.unlock();
			}
		}

		@Override
		public void write(
			final Object key,
			final VoidOperation op
		)
		{
			final Lock writeLock = this.stripes.get(key).writeLock();
			writeLock.lock();

			try
			{
				op.execute();
			}
			finally
			{
				writeLock.unlock();
			}
		}

	}


	public static abstract class Scope implements ReadWriteLockedStriped
	{
		private transient volatile ReadWriteLockedStriped delegate;

		protected Scope()
		{
			super();
		}

		protected ReadWriteLockedStriped delegate()
		{
			if(this.delegate == null)
			{
				synchronized(this)
				{
					if(this.delegate == null)
					{
						this.delegate = ReadWriteLockedStriped.New();
					}
				}
			}

			return this.delegate;
		}

		@Override
		public <T> T read(
			final Object key,
			final ValueOperation<T> op
		)
		{
			return this.delegate().read(key, op);
		}

		@Override
		public void read(
			final Object key,
			final VoidOperation op
		)
		{
			this.delegate().read(key, op);
		}

		@Override
		public <T> T write(
			final Object key,
			final ValueOperation<T> op
		)
		{
			return this.delegate().write(key, op);
		}

		@Override
		public void write(
			final Object key,
			final VoidOperation op
		)
		{
			this.delegate().write(key, op);
		}

	}

}
