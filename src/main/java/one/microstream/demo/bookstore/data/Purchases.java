
package one.microstream.demo.bookstore.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import one.microstream.reference.Lazy;
import one.microstream.storage.types.StorageConnection;


public interface Purchases
{
	public IntStream years();
	
	public void clear(
		int year
	);
	
	public Stream<Purchase> byYear(
		int year
	);
	
	public Stream<Purchase> byShopAndYear(
		Shop shop,
		int year
	);
	
	public Stream<Purchase> byShopsAndYear(
		Predicate<Shop> shopSelector,
		int year
	);
	
	public Stream<Purchase> byEmployeeAndYear(
		Employee employee,
		int year
	);
	
	public Stream<Purchase> byCustomerAndYear(
		Customer customer,
		int year
	);
	
	public static interface Mutable extends Purchases
	{
//		public void add(
//			Purchase purchase
//		);
		
		/*
		 * used by random data generator
		 */
		public Set<Customer> init(
			int year,
			List<Purchase> purchases,
			StorageConnection storer
		);
	}
	
	public static class Default implements Purchases.Mutable
	{
		static class YearlyPurchases
		{
			final Map<Shop,     Lazy<List<Purchase>>> shopToPurchases     = new HashMap<>(128);
			final Map<Employee, Lazy<List<Purchase>>> employeeToPurchases = new HashMap<>(512);
			final Map<Customer, Lazy<List<Purchase>>> customerToPurchases = new HashMap<>(1024);
			
			synchronized void add(
				final Purchase purchase
			)
			{
				this.internalAdd(this.shopToPurchases,     purchase.shop(),     purchase);
				this.internalAdd(this.employeeToPurchases, purchase.employee(), purchase);
				this.internalAdd(this.customerToPurchases, purchase.customer(), purchase);
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
			
			synchronized void clear()
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
		
//		@Override
//		public synchronized void add(
//			final Purchase purchase
//		)
//		{
//			int year = purchase.timestamp().getYear();
//			this.yearToPurchases.computeIfAbsent(year, y -> Lazy.Reference(new YearlyPurchases()))
//				.get()
//				.add(purchase);
//		}
		
		@Override
		public synchronized Set<Customer> init(
			final int year,
			final List<Purchase> purchases,
			final StorageConnection storer
		)
		{
			final YearlyPurchases yearlyPurchases = new YearlyPurchases();
			purchases.forEach(yearlyPurchases::add);
			
			final Lazy<YearlyPurchases> lazy = Lazy.Reference(yearlyPurchases);
			this.yearToPurchases.put(year, lazy);
			
			storer.store(this.yearToPurchases);
			
			final Set<Customer> customers = new HashSet<>(yearlyPurchases.customerToPurchases.keySet());
			
			yearlyPurchases.clear();
			lazy.clear();
			
			return customers;
		}
		
		@Override
		public void clear(
			final int year
		)
		{
			final Lazy<YearlyPurchases> lazy = this.yearToPurchases.get(year);
			if(lazy != null && lazy.isStored())
			{
				Optional.ofNullable(lazy.clear()).ifPresent(YearlyPurchases::clear);
			}
		}
		
		@Override
		public IntStream years()
		{
			return this.yearToPurchases.keySet().stream()
				.mapToInt(Integer::intValue)
				.sorted();
		}

		@Override
		public Stream<Purchase> byYear(
			final int year
		)
		{
			final YearlyPurchases yearlyPurchases = Lazy.get(this.yearToPurchases.get(year));
			return yearlyPurchases == null
				? Stream.empty()
				: yearlyPurchases.shopToPurchases.values().parallelStream()
					.map(l -> l.get())
					.flatMap(List::stream);
		}

		@Override
		public Stream<Purchase> byShopAndYear(
			final Shop shop,
			final int year
		)
		{
			final YearlyPurchases yearlyPurchases = Lazy.get(this.yearToPurchases.get(year));
			return yearlyPurchases == null
				? Stream.empty()
				: yearlyPurchases.byShop(shop);
		}
		
		@Override
		public Stream<Purchase> byShopsAndYear(
			final Predicate<Shop> shopSelector,
			final int year
		)
		{
			final YearlyPurchases yearlyPurchases = Lazy.get(this.yearToPurchases.get(year));
			return yearlyPurchases == null
				? Stream.empty()
				: yearlyPurchases.byShops(shopSelector);
		}

		@Override
		public Stream<Purchase> byEmployeeAndYear(
			final Employee employee,
			final int year
		)
		{
			final YearlyPurchases yearlyPurchases = Lazy.get(this.yearToPurchases.get(year));
			return yearlyPurchases == null
				? Stream.empty()
				: yearlyPurchases.byEmployee(employee);
		}

		@Override
		public Stream<Purchase> byCustomerAndYear(
			final Customer customer,
			final int year
		)
		{
			final YearlyPurchases yearlyPurchases = Lazy.get(this.yearToPurchases.get(year));
			return yearlyPurchases == null
				? Stream.empty()
				: yearlyPurchases.byCustomer(customer);
		}
	}
	
}
