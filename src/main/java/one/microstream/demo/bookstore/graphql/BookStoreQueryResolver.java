package one.microstream.demo.bookstore.graphql;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.kickstart.tools.GraphQLQueryResolver;
import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Book;
import one.microstream.demo.bookstore.data.BookSales;
import one.microstream.demo.bookstore.data.Country;
import one.microstream.demo.bookstore.data.Employee;
import one.microstream.demo.bookstore.data.Purchase;

@Component
public class BookStoreQueryResolver implements GraphQLQueryResolver
{
	@Autowired
	private BookStoreDemo bookStoreDemo;

	public BookStoreQueryResolver()
	{
		super();
	}

	private Country countryByCode(final String countryCode)
	{
		return this.bookStoreDemo.data().shops().compute(shops ->
			shops
				.map(s -> s.address().city().state().country())
				.filter(c -> c.code().equalsIgnoreCase(countryCode))
				.findAny()
				.orElse(null)
		);
	}

	public List<Book> booksByTitle(final String title)
	{
		return this.bookStoreDemo.data().books().searchByTitle(title);
	}

	public Employee employeeOfTheYear(final int year)
	{
		return this.bookStoreDemo.data().purchases().employeeOfTheYear(year);
	}

	public List<BookSales> bestSellerList(final int year)
	{
		return this.bookStoreDemo.data().purchases().bestSellerList(year);
	}

	public List<BookSales> bestSellerListByCountry(final int year, final String countryCode)
	{
		final Country country = this.countryByCode(countryCode);
		return country == null
			? Collections.emptyList()
			: this.bookStoreDemo.data().purchases().bestSellerList(year, country)
		;
	}

	public List<Purchase> purchasesOfForeigners(final int year)
	{
		return this.bookStoreDemo.data().purchases().purchasesOfForeigners(year);
	}

	public List<Purchase> purchasesOfForeignersByCountry(final int year, final String countryCode)
	{
		final Country country = this.countryByCode(countryCode);
		return country == null
			? Collections.emptyList()
			: this.bookStoreDemo.data().purchases().purchasesOfForeigners(year, country)
		;
	}

}

