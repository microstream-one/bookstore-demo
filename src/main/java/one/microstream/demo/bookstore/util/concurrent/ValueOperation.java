package one.microstream.demo.bookstore.util.concurrent;

@FunctionalInterface
public interface ValueOperation<T>
{
	public T execute();
}
