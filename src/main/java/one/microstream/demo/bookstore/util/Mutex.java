package one.microstream.demo.bookstore.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public interface Mutex
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


	public static Mutex New()
	{
		return new Default();
	}


	public static class Default implements Mutex
	{
		private final ReentrantReadWriteLock rrwLock = new ReentrantReadWriteLock();

		Default()
		{
			super();
		}

		@Override
		public <T> T read(final ValueOperation<T> op)
		{
			final ReadLock readLock = this.rrwLock.readLock();
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
			final ReadLock readLock = this.rrwLock.readLock();
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
			final WriteLock writeLock = this.rrwLock.writeLock();
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
			final WriteLock writeLock = this.rrwLock.writeLock();
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


	public static abstract class Owner implements Mutex
	{
		private transient volatile Mutex mutex;

		protected Owner()
		{
			super();
		}

		protected Mutex mutex()
		{
			if(this.mutex == null)
			{
				synchronized(this)
				{
					if(this.mutex == null)
					{
						this.mutex = Mutex.New();
					}
				}
			}

			return this.mutex;
		}

		@Override
		public <T> T read(final ValueOperation<T> op)
		{
			return this.mutex().read(op);
		}

		@Override
		public void read(final VoidOperation op)
		{
			this.mutex().read(op);
		}

		@Override
		public <T> T write(final ValueOperation<T> op)
		{
			return this.mutex().write(op);
		}

		@Override
		public void write(final VoidOperation op)
		{
			this.mutex().write(op);
		}

	}

}
