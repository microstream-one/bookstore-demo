
package one.microstream.demo.bookstore.dal;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static one.microstream.X.notNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import one.microstream.demo.bookstore.data.Author;
import one.microstream.demo.bookstore.data.Book;
import one.microstream.demo.bookstore.data.Country;
import one.microstream.demo.bookstore.data.Data;
import one.microstream.demo.bookstore.data.Employee;
import one.microstream.demo.bookstore.data.Genre;
import one.microstream.demo.bookstore.data.Language;
import one.microstream.demo.bookstore.data.Publisher;
import one.microstream.demo.bookstore.data.Purchase;
import one.microstream.demo.bookstore.data.Shop;
import one.microstream.demo.bookstore.data.Purchase.Item;


public interface DataAccess
{
	public Data data();

	public List<Book> booksByAuthor(
		Author author
	);

	public List<Book> booksByGenre(
		Genre genre
	);

	public List<Book> booksByPublisher(
		Publisher publisher
	);

	public List<Book> booksByLanguage(
		Language language
	);

	public List<BookSales> bestSellerList(
		int year
	);

	public List<BookSales> bestSellerList(
		int year,
		Country country
	);

	public List<Book> booksByTitle(
		String query
	);

	public List<Book> booksByTitle(
		String query,
		Country country
	);

	public List<Purchase> purchasesOfForeigners(
		int year
	);

	public List<Purchase> purchasesOfForeigners(
		int year,
		Country country
	);

	public long countPurchasesOfForeigners(
		int year
	);

	public long countPurchasesOfForeigners(
		int year,
		Country country
	);

	public double revenueOfShopInYear(
		Shop shop,
		int year
	);

	public Employee employeeOfTheYear(
		int year
	);

	public Employee employeeOfTheYear(
		int year,
		Country country
	);


	public static DataAccess New(final Data data)
	{
		return new Default(data);
	}


	public static class Default implements DataAccess
	{
		private final Data data;

		public Default(
			final Data data
		)
		{
			super();
			this.data = notNull(data);
		}

		@Override
		public Data data()
		{
			return this.data;
		}

		@Override
		public List<Book> booksByAuthor(
			final Author author
		)
		{
			return this.data.books()
				.byAuthor(author)
				.collect(toList());
		}

		@Override
		public List<Book> booksByGenre(
			final Genre genre
		)
		{
			return this.data.books()
				.byGenre(genre)
				.collect(toList());
		}

		@Override
		public List<Book> booksByPublisher(
			final Publisher publisher
		)
		{
			return this.data.books()
				.byPublisher(publisher)
				.collect(toList());
		}

		@Override
		public List<Book> booksByLanguage(
			final Language language
		)
		{
			return this.data.books()
				.byLanguage(language)
				.collect(toList());
		}

		@Override
		public List<BookSales> bestSellerList(
			final int year
		)
		{
			return this.bestSellerList(
				this.data.purchases().byYear(year)
			);
		}

		@Override
		public List<BookSales> bestSellerList(
			final int year,
			final Country country
		)
		{
			return this.bestSellerList(
				this.data.purchases().byShopsAndYear(
					shop -> shop.address().city().state().country() == country,
					year
				)
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
		public List<Book> booksByTitle(
			final String query
		)
		{
			return this.data.books()
				.searchByTitle(query)
				.collect(toList());
		}

		@Override
		public List<Book> booksByTitle(
			final String query,
			final Country country
		)
		{
			return this.data.books()
				.searchByTitle(query)
				.filter(book -> book.author().address().city().state().country() == country)
				.collect(toList());
		}

		@Override
		public long countPurchasesOfForeigners(
			final int year
		)
		{
			return this.purchasesOfForeignersStream(year)
				.count();
		}

		@Override
		public List<Purchase> purchasesOfForeigners(
			final int year
		)
		{
			return this.purchasesOfForeignersStream(year)
				.collect(toList());
		}

		private Stream<Purchase> purchasesOfForeignersStream(
			final int year
		)
		{
			return this.data.purchases().byYear(year)
				.filter(p -> p.customer().address().city() != p.shop().address().city());
		}

		@Override
		public long countPurchasesOfForeigners(
			final int year,
			final Country country
		)
		{
			return this.purchasesOfForeignersStream(year, country)
				.count();
		}

		@Override
		public List<Purchase> purchasesOfForeigners(
			final int year,
			final Country country
		)
		{
			return this.purchasesOfForeignersStream(year, country)
				.collect(toList());
		}

		private Stream<Purchase> purchasesOfForeignersStream(
			final int year,
			final Country country
		)
		{
			return this.data.purchases().byShopsAndYear(
					shop -> shop.address().city().state().country() == country,
					year
				)
				.filter(p -> p.customer().address().city() != p.shop().address().city());
		}

		@Override
		public double revenueOfShopInYear(
			final Shop shop,
			final int year
		)
		{
			return this.data.purchases()
				.byShopAndYear(shop, year)
				.mapToDouble(Purchase::total)
				.sum();
		}

		@Override
		public Employee employeeOfTheYear(
			final int year
		)
		{
			return maxKey(
				this.data.purchases()
					.byYear(year)
					.collect(
						groupingBy(
							Purchase::employee,
							summingDouble(Purchase::total)
						)
					)
				);
		}

		@Override
		public Employee employeeOfTheYear(
			final int year,
			final Country country
		)
		{
			return maxKey(
				this.data.purchases()
					.byShopsAndYear(
						shop -> shop.address().city().state().country() == country ,
						year
					)
					.collect(
						groupingBy(
							Purchase::employee,
							summingDouble(Purchase::total)
						)
					)
				);
		}

		private static <K> K maxKey(final Map<K, Double> map)
		{
			return map.entrySet()
				.parallelStream()
				.max((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
				.get()
				.getKey();
		}

	}

}
