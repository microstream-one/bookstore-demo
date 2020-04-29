
package one.microstream.demo.bookstore.data;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static one.microstream.demo.bookstore.util.CollectionUtils.ensureParallelStream;
import static one.microstream.demo.bookstore.util.CollectionUtils.maxKey;
import static one.microstream.demo.bookstore.util.CollectionUtils.summingMonetaryAmount;
import static org.javamoney.moneta.function.MonetaryFunctions.summarizingMonetary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.money.MonetaryAmount;

import com.google.common.collect.Range;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Purchase.Item;
import one.microstream.demo.bookstore.util.concurrent.ReadWriteLockedStriped;
import one.microstream.reference.Lazy;
import one.microstream.storage.types.StorageConnection;


public interface Purchases
{
	public Range<Integer> years();

	public void clear(
		int year
	);

	public <T> T computeByYear(int year, Function<Stream<Purchase>, T> streamFunction);

	public <T> T computeByShopAndYear(Shop shop, int year, Function<Stream<Purchase>, T> streamFunction);

	public <T> T computeByShopsAndYear(Predicate<Shop> shopSelector, int year, Function<Stream<Purchase>, T> streamFunction);

	public <T> T computeByEmployeeAndYear(Employee employee, int year, Function<Stream<Purchase>, T> streamFunction);

	public <T> T computeByCustomerAndYear(Customer customer, int year, Function<Stream<Purchase>, T> streamFunction);

	public List<BookSales> bestSellerList(int year);

	public List<BookSales> bestSellerList(int year, Country country);

	public long countPurchasesOfForeigners(int year);

	public List<Purchase> purchasesOfForeigners(int year);

	public long countPurchasesOfForeigners(int year, Country country);

	public List<Purchase> purchasesOfForeigners(int year, Country country);

	public MonetaryAmount revenueOfShopInYear(Shop shop, int year);

	public Employee employeeOfTheYear(int year);

	public Employee employeeOfTheYear(int year, Country country);

	public default void add(final Purchase purchase)
	{
		this.add(purchase, BookStoreDemo.getInstance().storageManager());
	}

	public void add(Purchase purchase, StorageConnection storage);


	public static class Default extends ReadWriteLockedStriped.Scope implements Purchases
	{
		static class YearlyPurchases
		{
			final Map<Shop,     Lazy<List<Purchase>>> shopToPurchases     = new HashMap<>(128);
			final Map<Employee, Lazy<List<Purchase>>> employeeToPurchases = new HashMap<>(512);
			final Map<Customer, Lazy<List<Purchase>>> customerToPurchases = new HashMap<>(1024);

			YearlyPurchases()
			{
				super();
			}

			YearlyPurchases add(
				final Purchase purchase
			)
			{
				addToMap(this.shopToPurchases,     purchase.shop(),     purchase);
				addToMap(this.employeeToPurchases, purchase.employee(), purchase);
				addToMap(this.customerToPurchases, purchase.customer(), purchase);
				return this;
			}

			private static <K> void addToMap(
				final Map<K, Lazy<List<Purchase>>> map,
				final K key,
				final Purchase purchase
			)
			{
				map.computeIfAbsent(key, k -> Lazy.Reference(new ArrayList<>(64)))
					.get()
					.add(purchase);
			}

			void clear()
			{
				clearMap(this.shopToPurchases);
				clearMap(this.employeeToPurchases);
				clearMap(this.customerToPurchases);
			}

			private static <K> void clearMap(
				final Map<K, Lazy<List<Purchase>>> map
			)
			{
				map.values().forEach(lazy ->
					Optional.ofNullable(lazy.clear()).ifPresent(List::clear)
				);
			}

			Stream<Purchase> byShop(
				final Shop shop
			)
			{
				return ensureParallelStream(
					Lazy.get(this.shopToPurchases.get(shop))
				);
			}

			Stream<Purchase> byShops(
				final Predicate<Shop> shopSelector
			)
			{
				return this.shopToPurchases.entrySet().parallelStream()
					.filter(e -> shopSelector.test(e.getKey()))
					.flatMap(e -> ensureParallelStream(Lazy.get(e.getValue())));
			}

			Stream<Purchase> byEmployee(
				final Employee employee
			)
			{
				return ensureParallelStream(
					Lazy.get(this.employeeToPurchases.get(employee))
				);
			}

			Stream<Purchase> byCustomer(
				final Customer customer
			)
			{
				return ensureParallelStream(
					Lazy.get(this.customerToPurchases.get(customer))
				);
			}

		}


		private final Map<Integer, Lazy<YearlyPurchases>> yearlyPurchases = new ConcurrentHashMap<>(32);

		Default()
		{
			super();
		}

		@Override
		public void add(
			final Purchase purchase,
			final StorageConnection storage
		)
		{
			final Integer year = purchase.timestamp().getYear();
			this.write(year, () ->
			{
				final Lazy<YearlyPurchases> lazy = this.yearlyPurchases.get(year);
				if(lazy != null)
				{
					storage.store(lazy.get().add(purchase));
				}
				else
				{
					this.yearlyPurchases.put(
						year,
						Lazy.Reference(
							new YearlyPurchases().add(purchase)
						)
					);
					storage.store(this.yearlyPurchases);
				}
			});
		}

		Set<Customer> init(
			final int year,
			final List<Purchase> purchases,
			final StorageConnection storage
		)
		{
			return this.write(year, () ->
			{
				final YearlyPurchases yearlyPurchases = new YearlyPurchases();
				purchases.forEach(yearlyPurchases::add);

				final Lazy<YearlyPurchases> lazy = Lazy.Reference(yearlyPurchases);
				this.yearlyPurchases.put(year, lazy);

				storage.store(this.yearlyPurchases);

				final Set<Customer> customers = new HashSet<>(yearlyPurchases.customerToPurchases.keySet());

				yearlyPurchases.clear();
				lazy.clear();

				return customers;
			});
		}

		@Override
		public void clear(
			final int year
		)
		{
			this.write(year, () ->
			{
				final Lazy<YearlyPurchases> lazy = this.yearlyPurchases.get(year);
				if(lazy != null && lazy.isStored())
				{
					Optional.ofNullable(lazy.clear()).ifPresent(YearlyPurchases::clear);
				}
			});
		}

		@Override
		public Range<Integer> years()
		{
			final IntSummaryStatistics summary = this.yearlyPurchases.keySet().stream()
				.mapToInt(Integer::intValue)
				.summaryStatistics();
			return Range.closed(summary.getMin(), summary.getMax());
		}

		@Override
		public <T> T computeByYear(
			final int year,
			final Function<Stream<Purchase>, T> streamFunction
		)
		{
			return this.read(year, () ->
			{
				final YearlyPurchases yearlyPurchases = Lazy.get(this.yearlyPurchases.get(year));
				return streamFunction.apply(
					yearlyPurchases == null
						? Stream.empty()
						: yearlyPurchases.shopToPurchases.values().parallelStream()
							.map(l -> l.get())
							.flatMap(List::stream)
				);
			});
		}

		@Override
		public <T> T computeByShopAndYear(
			final Shop shop,
			final int year,
			final Function<Stream<Purchase>, T> streamFunction
		)
		{
			return this.read(year, () ->
			{
				final YearlyPurchases yearlyPurchases = Lazy.get(this.yearlyPurchases.get(year));
				return streamFunction.apply(
					yearlyPurchases == null
						? Stream.empty()
						: yearlyPurchases.byShop(shop)
				);
			});
		}

		@Override
		public <T> T computeByShopsAndYear(
			final Predicate<Shop> shopSelector,
			final int year,
			final Function<Stream<Purchase>, T> streamFunction
		)
		{
			return this.read(year, () ->
			{
				final YearlyPurchases yearlyPurchases = Lazy.get(this.yearlyPurchases.get(year));
				return streamFunction.apply(
					yearlyPurchases == null
						? Stream.empty()
						: yearlyPurchases.byShops(shopSelector)
				);
			});
		}

		@Override
		public <T> T computeByEmployeeAndYear(
			final Employee employee,
			final int year,
			final Function<Stream<Purchase>, T> streamFunction
		)
		{
			return this.read(year, () ->
			{
				final YearlyPurchases yearlyPurchases = Lazy.get(this.yearlyPurchases.get(year));
				return streamFunction.apply(
					yearlyPurchases == null
						? Stream.empty()
						: yearlyPurchases.byEmployee(employee)
				);
			});
		}

		@Override
		public <T> T computeByCustomerAndYear(
			final Customer customer,
			final int year,
			final Function<Stream<Purchase>, T> streamFunction
		)
		{
			return this.read(year, () ->
			{
				final YearlyPurchases yearlyPurchases = Lazy.get(this.yearlyPurchases.get(year));
				return streamFunction.apply(
					yearlyPurchases == null
						? Stream.empty()
						: yearlyPurchases.byCustomer(customer)
				);
			});
		}

		@Override
		public List<BookSales> bestSellerList(
			final int year
		)
		{
			return this.computeByYear(
				year,
				this::bestSellerList
			);
		}

		@Override
		public List<BookSales> bestSellerList(
			final int year,
			final Country country
		)
		{
			return this.computeByShopsAndYear(
				shopInCountryPredicate(country),
				year,
				this::bestSellerList
			);
		}

		private List<BookSales> bestSellerList(
			final Stream<Purchase> purchases
		)
		{
			return purchases
				.flatMap(Purchase::items)
				.collect(
					groupingBy(
						Item::book,
						summingInt(Item::amount)
					)
				)
				.entrySet()
				.stream()
				.map(e -> BookSales.New(e.getKey(), e.getValue()))
				.sorted()
				.collect(toList());
		}

		@Override
		public long countPurchasesOfForeigners(
			final int year
		)
		{
			return this.computePurchasesOfForeigners(
				year,
				purchases -> purchases.count()
			);
		}

		@Override
		public List<Purchase> purchasesOfForeigners(
			final int year
		)
		{
			return this.computePurchasesOfForeigners(
				year,
				purchases -> purchases.collect(toList())
			);
		}

		private <T> T computePurchasesOfForeigners(
			final int year,
			final Function <Stream<Purchase>, T> streamFunction
		)
		{
			return this.computeByYear(
				year,
				purchases -> streamFunction.apply(
					purchases.filter(
						purchaseOfForeignerPredicate()
					)
				)
			);
		}

		@Override
		public long countPurchasesOfForeigners(
			final int year,
			final Country country
		)
		{
			return this.computePurchasesOfForeigners(
				year,
				country,
				purchases -> purchases.count()
			);
		}

		@Override
		public List<Purchase> purchasesOfForeigners(
			final int year,
			final Country country
		)
		{
			return this.computePurchasesOfForeigners(
				year,
				country,
				purchases -> purchases.collect(toList())
			);
		}

		private <T> T computePurchasesOfForeigners(
			final int year,
			final Country country,
			final Function <Stream<Purchase>, T> streamFunction
		)
		{
			return this.computeByShopsAndYear(
				shopInCountryPredicate(country),
				year,
				purchases -> streamFunction.apply(
					purchases.filter(
						purchaseOfForeignerPredicate()
					)
				)
			);
		}

		private static Predicate<Shop> shopInCountryPredicate(final Country country)
		{
			return shop -> shop.address().city().state().country() == country;
		}

		private static Predicate<? super Purchase> purchaseOfForeignerPredicate()
		{
			return p -> p.customer().address().city() != p.shop().address().city();
		}

		@Override
		public MonetaryAmount revenueOfShopInYear(
			final Shop shop,
			final int year
		)
		{
			return this.computeByShopAndYear(
				shop,
				year,
				purchases -> purchases
					.map(Purchase::total)
					.collect(summarizingMonetary(BookStoreDemo.currencyUnit()))
					.getSum()
			);
		}

		@Override
		public Employee employeeOfTheYear(
			final int year
		)
		{
			return this.computeByYear(
				year,
				bestPerformingEmployeeFunction()
			);
		}

		@Override
		public Employee employeeOfTheYear(
			final int year,
			final Country country
		)
		{
			return this.computeByShopsAndYear(
				shopInCountryPredicate(country) ,
				year,
				bestPerformingEmployeeFunction()
			);
		}

		private static Function<Stream<Purchase>, Employee> bestPerformingEmployeeFunction()
		{
			return purchases -> maxKey(
				purchases.collect(
					groupingBy(
						Purchase::employee,
						summingMonetaryAmount(
							BookStoreDemo.currencyUnit(),
							Purchase::total
						)
					)
				)
			);
		}

	}

}
