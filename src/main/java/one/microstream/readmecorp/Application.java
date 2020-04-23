package one.microstream.readmecorp;

import one.microstream.readmecorp.data.RandomDataAmount;

public class Application
{
	public static void main(final String[] args)
	{
		final ReadMeCorp readMeCorp = new ReadMeCorp(
			RandomDataAmount.Medium()
		);

		new Repl(readMeCorp).run();
	}
}
