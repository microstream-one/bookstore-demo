package one.microstream.demo.bookstore.graphql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;
import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Country;

/**
 * GraphQL data fetchers mapped to the data layer.
 *
 */
@Component
public class GraphqlDataFetchers
{
	@Autowired
	private BookStoreDemo bookStoreDemo;

	public GraphqlDataFetchers()
	{
		super();
	}

	private Country countryByCode(
		final String countryCode
	)
	{
		return this.bookStoreDemo.data().shops().compute(shops ->
			shops
				.map(s -> s.address().city().state().country())
				.filter(c -> c.code().equalsIgnoreCase(countryCode))
				.findAny()
				.orElse(null)
		);
	}

	public DataFetcher<?> booksByTitle()
	{
		return environment ->
		{
			final String title = environment.getArgument("title");
			return this.bookStoreDemo.data().books().searchByTitle(title);
		};
	}

	public DataFetcher<?> employeeOfTheYear()
	{
		return environment ->
		{
			final int year = environment.getArgument("year");
			return this.bookStoreDemo.data().purchases().employeeOfTheYear(year);
		};
	}

	public DataFetcher<?> bestSellerList()
	{
		return environment ->
		{
			final int year = environment.getArgument("year");
			return this.bookStoreDemo.data().purchases().bestSellerList(year);
		};
	}

	public DataFetcher<?> bestSellerListByCountry()
	{
		return environment ->
		{
			final int year = environment.getArgument("year");
			final String countryCode = environment.getArgument("country");
			final Country country = this.countryByCode(countryCode);
			return country == null
				? null
				: this.bookStoreDemo.data().purchases().bestSellerList(year, country)
			;
		};
	}

	public DataFetcher<?> purchasesOfForeigners()
	{
		return environment ->
		{
			final int year = environment.getArgument("year");
			return this.bookStoreDemo.data().purchases().purchasesOfForeigners(year);
		};
	}

	public DataFetcher<?> purchasesOfForeignersByCountry()
	{
		return environment ->
		{
			final int year = environment.getArgument("year");
			final String countryCode = environment.getArgument("country");
			final Country country = this.countryByCode(countryCode);
			return country == null
				? null
				: this.bookStoreDemo.data().purchases().purchasesOfForeigners(year, country);
		};
	}

}
