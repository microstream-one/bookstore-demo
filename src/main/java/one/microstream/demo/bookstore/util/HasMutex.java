package one.microstream.demo.bookstore.util;

import java.util.function.Supplier;

public abstract class HasMutex implements Mutex
{
	private transient volatile Mutex mutex;

	protected HasMutex()
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
	public <T> T read(final Supplier<T> op)
	{
		return this.mutex().read(op);
	}

	@Override
	public void read(final Runnable op)
	{
		this.mutex().read(op);
	}

	@Override
	public <T> T write(final Supplier<T> op)
	{
		return this.mutex().write(op);
	}

	@Override
	public void write(final Runnable op)
	{
		this.mutex().write(op);
	}

}
