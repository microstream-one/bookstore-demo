package one.microstream.demo.bookstore.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.function.Supplier;

public interface Mutex
{
	public <T> T read(Supplier<T> op);

	public void read(Runnable op);

	public <T> T write(Supplier<T> op);

	public void write(Runnable op);


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
		public <T> T read(final Supplier<T> op)
		{
			final ReadLock readLock = this.rrwLock.readLock();
			readLock.lock();

			try
			{
				return op.get();
			}
			finally
			{
				readLock.unlock();
			}
		}

		@Override
		public void read(final Runnable op)
		{
			final ReadLock readLock = this.rrwLock.readLock();
			readLock.lock();

			try
			{
				op.run();
			}
			finally
			{
				readLock.unlock();
			}
		}

		@Override
		public <T> T write(final Supplier<T> op)
		{
			final WriteLock writeLock = this.rrwLock.writeLock();
			writeLock.lock();

			try
			{
				return op.get();
			}
			finally
			{
				writeLock.unlock();
			}
		}

		@Override
		public void write(final Runnable op)
		{
			final WriteLock writeLock = this.rrwLock.writeLock();
			writeLock.lock();

			try
			{
				op.run();
			}
			finally
			{
				writeLock.unlock();
			}
		}

	}

}
