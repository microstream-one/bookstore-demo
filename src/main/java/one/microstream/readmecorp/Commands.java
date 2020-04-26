package one.microstream.readmecorp;

import java.time.Year;
import java.util.List;

import one.microstream.readmecorp.dal.BookSales;
import one.microstream.readmecorp.dal.DataAccess;
import one.microstream.readmecorp.data.Book;
import one.microstream.readmecorp.data.Country;
import one.microstream.readmecorp.data.Employee;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;


@Command(name = "")
public class Commands
{
	public static CommandLine createCommandLine(final ReadMeCorp readMeCorp)
	{
		final CommandLine cli = new CommandLine(new Commands());
		cli.addSubcommand(new Books(readMeCorp));
		cli.addSubcommand(new BestSellerList(readMeCorp));
		cli.addSubcommand(new PurchasesOfForeigners(readMeCorp));
		cli.addSubcommand(new EmployeeOfTheYear(readMeCorp));
		cli.addSubcommand(new Exit(readMeCorp));
		return cli;
	}

	static abstract class Abstract implements Runnable
	{
		final ReadMeCorp readMeCorp;

		Abstract(final ReadMeCorp readMeCorp)
		{
			this.readMeCorp = readMeCorp;
		}

		DataAccess dataAccess()
		{
			return DataAccess.New(this.readMeCorp.data());
		}
	}

	@Command(
		name = "books",
		description = "Prints the books found by the given query."
	)
	static class Books extends Abstract
	{
		@Option(
			names = {"--query", "-q"},
			description = "the title query",
			required = true
		)
		String query;

		Books(final ReadMeCorp readMeCorp)
		{
			super(readMeCorp);
		}

		@Override
		public void run()
		{
			final List<Book> books = this.dataAccess().booksByTitle(this.query);
			if(books.isEmpty())
			{
				System.out.println("No books found");
			}
			else
			{
				books.stream().forEach(book ->
				{
					System.out.println(book.title() + "; by " + book.author().name());
				});
			}
		}
	}

	@Command(
		name = "bestSellerList",
		aliases = {"bsl"},
		description = "Prints the top ten best selling books of the given year."
	)
	static class BestSellerList extends Abstract
	{
		@Option(
			names = {"--year", "-y"},
			description = "the year for the best seller list",
			required = false,
			defaultValue = "0"
		)
		int    year;

		@Option(
			names = {"--country", "-c"},
			description = " ISO 3166 alpha-2 country code",
			required = false,
			defaultValue = ""
		)
		String country;

		BestSellerList(final ReadMeCorp readMeCorp)
		{
			super(readMeCorp);
		}

		@Override
		public void run()
		{
			final DataAccess dataAccess = this.dataAccess();

			final int year = this.year == 0
				? Year.now().getValue()
				: this.year;

			if(this.country.isEmpty())
			{
				final List<BookSales> bestSellerList = dataAccess.bestSellerList(year);
				if(bestSellerList.isEmpty())
				{
					System.out.println("No books sold in " + year);
				}
				else
				{
					System.out.println("Best selling books in " + year);
					bestSellerList.stream().limit(10).forEach(sales ->
					{
						System.out.println(sales.amount() + " "
							+ sales.book().title() + "; by "
							+ sales.book().author().name()
						);
					});
				}
			}
			else
			{
				final Country country = dataAccess.data()
					.shops()
					.map(s -> s.address().city().state().country())
					.distinct()
					.filter(c -> c.code().equalsIgnoreCase(this.country))
					.findAny()
					.orElse(null);

				if(country == null)
				{
					System.out.println("Country not found");
				}
				else
				{
					final List<BookSales> bestSellerList = dataAccess.bestSellerList(year, country);
					if(bestSellerList.isEmpty())
					{
						System.out.println("No books sold in " + country.name() + " in " + year);
					}
					else
					{
						System.out.println("Best selling books in " + country.name() + " in " + year);
						bestSellerList.stream().limit(10).forEach(sales ->
						{
							System.out.println(sales.amount() + " "
								+ sales.book().title() + "; by "
								+ sales.book().author().name()
							);
						});
					}
				}
			}
		}
	}

	@Command(
		name = "purchasesOfForeigners",
		aliases = {"pof"},
		description = "Prints the count of purchases of foreigners."
	)
	static class PurchasesOfForeigners extends Abstract
	{
		@Option(
			names = {"--year", "-y"},
			description = "the year for the calculation",
			required = false,
			defaultValue = "0"
		)
		int    year;

		@Option(
			names = {"--country", "-c"},
			description = " ISO 3166 alpha-2 country code",
			required = false,
			defaultValue = ""
		)
		String country;

		PurchasesOfForeigners(final ReadMeCorp readMeCorp)
		{
			super(readMeCorp);
		}

		@Override
		public void run()
		{
			final DataAccess dataAccess = this.dataAccess();

			final int year = this.year == 0
				? Year.now().getValue()
				: this.year;

			if(this.country.isEmpty())
			{
				final long count = dataAccess.countPurchasesOfForeigners(year);
				System.out.println("Purchases of foreigners in " + year);
				System.out.println(count);
			}
			else
			{
				final Country country = dataAccess.data()
					.shops()
					.map(s -> s.address().city().state().country())
					.distinct()
					.filter(c -> c.code().equalsIgnoreCase(this.country))
					.findAny()
					.orElse(null);

				if(country == null)
				{
					System.out.println("Country not found");
				}
				else
				{
					final long count = dataAccess.countPurchasesOfForeigners(year, country);
					System.out.println("Purchases of foreigners in " + country.name() + " in " + year);
					System.out.println(count);
				}
			}
		}
	}

	@Command(
		name = "employeeOfTheYear",
		aliases = {"eoty"},
		description = "Prints the best performing employee of a specific country or worldwide."
	)
	static class EmployeeOfTheYear extends Abstract
	{
		@Option(
			names = {"--year", "-y"},
			description = "the year for the calculation",
			required = false,
			defaultValue = "0"
		)
		int    year;

		@Option(
			names = {"--country", "-c"},
			description = " ISO 3166 alpha-2 country code",
			required = false,
			defaultValue = ""
		)
		String country;

		EmployeeOfTheYear(final ReadMeCorp readMeCorp)
		{
			super(readMeCorp);
		}

		@Override
		public void run()
		{
			final DataAccess dataAccess = this.dataAccess();

			final int year = this.year == 0
				? Year.now().getValue()
				: this.year;

			if(this.country.isEmpty())
			{
				final Employee employee = dataAccess.employeeOfTheYear(year);
				System.out.println("Employee of the year " + year);
				System.out.println(employee.name());
			}
			else
			{
				final Country country = dataAccess.data()
					.shops()
					.map(s -> s.address().city().state().country())
					.distinct()
					.filter(c -> c.code().equalsIgnoreCase(this.country))
					.findAny()
					.orElse(null);

				if(country == null)
				{
					System.out.println("Country not found");
				}
				else
				{
					final Employee employee = dataAccess.employeeOfTheYear(year, country);
					System.out.println("Employee of the year " + year + " in " + country.name());
					System.out.println(employee.name());
				}
			}
		}
	}

	@Command(
		name = "exit",
		aliases = {"quit"}
	)
	static class Exit extends Abstract
	{
		Exit(final ReadMeCorp readMeCorp)
		{
			super(readMeCorp);
		}

		@Override
		public void run()
		{
			this.readMeCorp.shutdown();

			System.out.println("Bye!");

			System.exit(0);
		}
	}

}
