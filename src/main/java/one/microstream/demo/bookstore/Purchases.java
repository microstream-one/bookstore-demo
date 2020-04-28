
package one.microstream.demo.bookstore;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import one.microstream.demo.bookstore.Purchase.Item;
import one.microstream.reference.Lazy;
import one.microstream.storage.types.StorageConnection;


public interface Purchases
{
	public IntStream years();

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

	public double revenueOfShopInYear(Shop shop, int year);

	public Employee employeeOfTheYear(int year);

	public Employee employeeOfTheYear(int year, Country country);

	public void add(Purchase purchase, StorageConnection storage);


	public static class Default extends HasMutex implements Purchases
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
				this.internalAdd(this.shopToPurchases,     purchase.shop(),     purchase);
				this.internalAdd(this.employeeToPurchases, purchase.employee(), purchase);
				this.internalAdd(this.customerToPurchases, purchase.customer(), purchase);
				return this;
			}

			private <K> void internalAdd(
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
				this.shopToPurchases    .values().forEach(l -> Optional.ofNullable(l.clear()).ifPresent(List::clear));
				this.employeeToPurchases.values().forEach(l -> Optional.ofNullable(l.clear()).ifPresent(List::clear));
				this.customerToPurchases.values().forEach(l -> Optional.ofNullable(l.clear()).ifPresent(List::clear));
			}

			Stream<Purchase> byShop(
				final Shop shop
			)
			{
				final List<Purchase> list = Lazy.get(this.shopToPurchases.get(shop));
				return list == null
					? Stream.empty()
					: list.parallelStream();
			}

			Stream<Purchase> byShops(
				final Predicate<Shop> shopSelector
			)
			{
				return this.shopToPurchases.entrySet().parallelStream()
					.filter(e -> shopSelector.test(e.getKey()))
					.flatMap(e -> {
						final List<Purchase> list = Lazy.get(e.getValue());
						return list == null
							? Stream.empty()
							: list.stream();
					});
			}

			Stream<Purchase> byEmployee(
				final Employee employee
			)
			{
				final List<Purchase> list = Lazy.get(this.employeeToPurchases.get(employee));
				return list == null
					? Stream.empty()
					: list.parallelStream();
			}

			Stream<Purchase> byCustomer(
				final Customer customer
			)
			{
				final List<Purchase> list = Lazy.get(this.customerToPurchases.get(customer));
				return list == null
					? Stream.empty()
					: list.parallelStream();
			}

		}


		private final Map<Integer, Lazy<YearlyPurchases>> yearToPurchases = new HashMap<>(32);

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
			this.write(() ->
			{
				final Integer               year = purchase.timestamp().getYear();
				final Lazy<YearlyPurchases> lazy = this.yearToPurchases.get(year);
				if(lazy != null)
				{
					storage.store(lazy.get().add(purchase));
				}
				else
				{
					this.yearToPurchases.put(
						year,
						Lazy.Reference(
							new YearlyPurchases().add(purchase)
						)
					);
					storage.store(this.yearToPurchases);
				}
			});
		}

		Set<Customer> init(
			final int year,
			final List<Purchase> purchases,
			final StorageConnection storage
		)
		{
			return this.write(() ->
			{
				final YearlyPurchases yearlyPurchases = new YearlyPurchases();
				purchases.forEach(yearlyPurchases::add);

				final Lazy<YearlyPurchases> lazy = Lazy.Reference(yearlyPurchases);
				this.yearToPurchases.put(year, lazy);

				storage.store(this.yearToPurchases);

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
			this.write(() ->
			{
				final Lazy<YearlyPurchases> lazy = this.yearToPurchases.get(year);
				if(lazy != null && lazy.isStored())
				{
					Optional.ofNullable(lazy.clear()).ifPresent(YearlyPurchases::clear);
				}
			});
		}

		@Override
		public IntStream years()
		{
			return this.read(() ->
				this.yearToPurchases.keySet().stream()
					.mapToInt(Integer::intValue)
					.sorted()
			);
		}

		@Override
		public <T> T computeByYear(
			final int year,
			final Function<Stream<Purchase>, T> streamFunction
		)
		{
			return this.read(() ->
			{
				final YearlyPurchases yearlyPurchases = Lazy.get(this.yearToPurchases.get(year));
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
			return this.read(() ->
			{
				final YearlyPurchases yearlyPurchases = Lazy.get(this.yearToPurchases.get(year));
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
			return this.read(() ->
			{
				final YearlyPurchases yearlyPurchases = Lazy.get(this.yearToPurchases.get(year));
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
			return this.read(() ->
			{
				final YearlyPurchases yearlyPurchases = Lazy.get(this.yearToPurchases.get(year));
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
			return this.read(() ->
			{
				final YearlyPurchases yearlyPurchases = Lazy.get(this.yearToPurchases.get(year));
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
				.map(e -> new BookSales(e.getKey(), e.getValue()))
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
		public double revenueOfShopInYear(
			final Shop shop,
			final int year
		)
		{
			return this.computeByShopAndYear(
				shop,
				year,
				purchases -> purchases
					.mapToDouble(Purchase::total)
					.sum()
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
						summingDouble(Purchase::total)
					)
				)
			);
		}

		private static <K> K maxKey(
			final Map<K, Double> map
		)
		{
			return map.entrySet()
				.parallelStream()
				.max((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
				.get()
				.getKey();
		}

	}

}
