package one.microstream.demo.bookstore.util.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public interface ReadWriteLocked
{
	public static interface ValueOperation<T>
	{
		public T execute();
	}

	public static interface VoidOperation
	{
		public void execute();
	}


	public <T> T read(ValueOperation<T> op);

	public void read(VoidOperation op);

	public <T> T write(ValueOperation<T> op);

	public void write(VoidOperation op);


	public static ReadWriteLocked New()
	{
		return new Default();
	}


	public static class Default implements ReadWriteLocked
	{
		private final ReentrantReadWriteLock mutex = new ReentrantReadWriteLock();

		Default()
		{
			super();
		}

		@Override
		public <T> T read(final ValueOperation<T> op)
		{
			final ReadLock readLock = this.mutex.readLock();
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
		public void read(final VoidOperation op)
		{
			final ReadLock readLock = this.mutex.readLock();
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
		public <T> T write(final ValueOperation<T> op)
		{
			final WriteLock writeLock = this.mutex.writeLock();
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
		public void write(final VoidOperation op)
		{
			final WriteLock writeLock = this.mutex.writeLock();
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


	public static abstract class Scope implements ReadWriteLocked
	{
		private transient volatile ReadWriteLocked delegate;

		protected Scope()
		{
			super();
		}

		protected ReadWriteLocked delegate()
		{
			if(this.delegate == null)
			{
				synchronized(this)
				{
					if(this.delegate == null)
					{
						this.delegate = ReadWriteLocked.New();
					}
				}
			}

			return this.delegate;
		}

		@Override
		public <T> T read(final ValueOperation<T> op)
		{
			return this.delegate().read(op);
		}

		@Override
		public void read(final VoidOperation op)
		{
			this.delegate().read(op);
		}

		@Override
		public <T> T write(final ValueOperation<T> op)
		{
			return this.delegate().write(op);
		}

		@Override
		public void write(final VoidOperation op)
		{
			this.delegate().write(op);
		}

	}

}
