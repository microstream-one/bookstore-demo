package one.microstream.demo.bookstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import one.microstream.storage.types.StorageConnection;

public interface Shops
{
	public int shopCount();

	public void clear();

	public <T> T compute(Function<Stream<Shop>, T> streamFunction);

	public void add(Shop shop, StorageConnection storage);

	public void addAll(Collection<? extends Shop> shops, StorageConnection storage);


	public static class Default extends HasMutex implements Shops
	{
		private final List<Shop> shops = new ArrayList<>(1024);

		Default()
		{
			super();
		}

		@Override
		public int shopCount()
		{
			return this.read(
				this.shops::size
			);
		}

		@Override
		public void clear()
		{
			this.write(() ->
				this.shops.forEach(Shop::clear)
			);
		}

		@Override
		public <T> T compute(
			final Function<Stream<Shop>, T> streamFunction
		)
		{
			return this.read(() ->
				streamFunction.apply(
					this.shops.parallelStream()
				)
			);
		}

		@Override
		public void add(
			final Shop shop,
			final StorageConnection storage
		)
		{
			this.write(() -> {
				this.shops.add(shop);
				storage.store(this.shops);
			});
		}

		@Override
		public void addAll(
			final Collection<? extends Shop> shops,
			final StorageConnection storage
		)
		{
			this.write(() -> {
				this.shops.addAll(shops);
				storage.store(this.shops);
			});
		}

	}

}
