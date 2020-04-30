
package one.microstream.demo.bookstore;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.Locale;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.rapidpm.dependencies.core.logger.HasLogger;

import one.microstream.demo.bookstore.data.Data;
import one.microstream.demo.bookstore.data.DataMetrics;
import one.microstream.demo.bookstore.data.RandomDataAmount;
import one.microstream.jdk8.java.util.BinaryHandlersJDK8;
import one.microstream.storage.configuration.Configuration;
import one.microstream.storage.types.EmbeddedStorageFoundation;
import one.microstream.storage.types.EmbeddedStorageManager;


public final class BookStoreDemo implements HasLogger
{
	private static BookStoreDemo instance;

	public static BookStoreDemo getInstance()
	{
		return instance;
	}


	public static CurrencyUnit currencyUnit()
	{
		return Monetary.getCurrency(Locale.US);
	}

	public static MonetaryAmount money(final double number)
	{
		return Money.of(number, currencyUnit());
	}

	public static MonetaryAmount money(final BigDecimal number)
	{
		return Money.of(number, currencyUnit());
	}

	public static MonetaryAmount retailPrice(
		final MonetaryAmount purchasePrice
	)
	{
		return purchasePrice.multiply(1.11);
	}


	private final RandomDataAmount initialDataAmount;
	private EmbeddedStorageManager storageManager;

	public BookStoreDemo(final RandomDataAmount initialDataAmount)
	{
		super();
		this.initialDataAmount = initialDataAmount;
		BookStoreDemo.instance = this;
	}

	public EmbeddedStorageManager storageManager()
	{
		if(this.storageManager == null)
		{
			this.logger().info("Initializing MicroStream StorageManager");

			final Configuration configuration = Configuration.Default();
			configuration.setBaseDirectory(Paths.get("data", "storage").toString());
			configuration.setChannelCount(Integer.highestOneBit(Runtime.getRuntime().availableProcessors() - 1));

			final EmbeddedStorageFoundation<?> foundation = configuration.createEmbeddedStorageFoundation();
			foundation.onConnectionFoundation(BinaryHandlersJDK8::registerJDK8TypeHandlers);
			this.storageManager = foundation.createEmbeddedStorageManager().start();

			if(this.storageManager.root() == null)
			{
				this.logger().info("No data found, initializing random data");

				final Data.Default data = Data.New();
				this.storageManager.setRoot(data);
				this.storageManager.storeRoot();
				final DataMetrics metrics = data.populate(
					this.initialDataAmount,
					this.storageManager
				);

				this.logger().info("Random data generated: " + metrics.toString());
			}
		}

		return this.storageManager;
	}

	public Data data()
	{
		return (Data)this.storageManager().root();
	}

	public void shutdown()
	{
		if(this.storageManager != null)
		{
			this.storageManager.shutdown();
			this.storageManager = null;
		}
	}

}
