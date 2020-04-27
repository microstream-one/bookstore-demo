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


	public static interface Mutable extends Shops
	{
		public void add(Shop shop, StorageConnection storage);

		public void addAll(Collection<? extends Shop> shops, StorageConnection storage);
	}

	public static class Default implements Shops.Mutable
	{
		private final List<Shop> shops = new ArrayList<>(1024);

		Default()
		{
			super();
		}

		@Override
		public synchronized int shopCount()
		{
			return this.shops.size();
		}

		@Override
		public synchronized void clear()
		{
			this.shops.forEach(Shop::clear);
		}

		@Override
		public synchronized <T> T compute(
			final Function<Stream<Shop>, T> streamFunction
		)
		{
			return streamFunction.apply(
				this.shops.parallelStream()
			);
		}

		@Override
		public synchronized void add(
			final Shop shop,
			final StorageConnection storage
		)
		{
			this.shops.add(shop);
			storage.store(this.shops);
		}

		@Override
		public synchronized void addAll(
			final Collection<? extends Shop> shops,
			final StorageConnection storage
		)
		{
			this.shops.addAll(shops);
			storage.store(this.shops);
		}

	}

}
