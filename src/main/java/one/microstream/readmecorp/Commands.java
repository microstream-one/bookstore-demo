package one.microstream.readmecorp;

import java.util.List;

import one.microstream.readmecorp.dal.BookSales;
import one.microstream.readmecorp.dal.DataAccess;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;


@Command(name = "")
public class Commands
{
	public static CommandLine createCommandLine(final ReadMeCorp readMeCorp)
	{
		final CommandLine cli = new CommandLine(new Commands());
		cli.addSubcommand(new BestSellerList(readMeCorp));
		cli.addSubcommand(new Exit(readMeCorp));
		return cli;
	}


	static abstract class Abstract implements Runnable
	{
		final ReadMeCorp   readMeCorp;

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
		name = "bestSellerList",
		aliases = {"bsl"},
		description = "Prints the top ten best selling books of the given year."
	)
	static class BestSellerList extends Abstract
	{
		@Parameters(
			index = "0",
			paramLabel = "year",
			description = "the required year"
		)
		int year;

		BestSellerList(final ReadMeCorp readMeCorp)
		{
			super(readMeCorp);
		}

		@Override
		public void run()
		{
			final List<BookSales> bestSellerList = this.dataAccess().bestSellerList(this.year);
			if(bestSellerList.isEmpty())
			{
				System.out.println("No books sold in " + this.year);
			}
			else
			{
				bestSellerList.stream().limit(10).forEach(sales -> {
					System.out.println(sales.amount() + " " + sales.book().title());
				});
			}
		}
	}


	@Command(name = "exit", aliases = {"quit"})
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
