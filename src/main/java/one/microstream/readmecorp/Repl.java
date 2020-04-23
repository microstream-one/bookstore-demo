package one.microstream.readmecorp;

import java.io.Console;
import java.util.List;
import java.util.Scanner;

import org.jline.reader.ParsedLine;
import org.jline.reader.Parser;
import org.jline.reader.impl.DefaultParser;

import picocli.CommandLine;


public class Repl implements Runnable
{
	private final ReadMeCorp readMeCorp;
	private Console          console;
	private Scanner          scanner;


	public Repl(final ReadMeCorp readMeCorp)
	{
		this.readMeCorp = readMeCorp;

		if((this.console = System.console()) == null)
		{
			this.scanner = new Scanner(System.in);
		}
	}

	@Override
	public void run()
	{
		// initialize firstexit
		this.readMeCorp.data();

		final CommandLine cli    = Commands.createCommandLine(this.readMeCorp);
		final Parser      parser = new DefaultParser();

		cli.usage(System.out);

		while(true)
		{
			System.out.print("ReadMeCorp > ");

			final String       line       = this.readLine();
			final ParsedLine   parsedLine = parser.parse(line, line.length());
			final List<String> words      = parsedLine.words();
			cli.execute(words.toArray(new String[words.size()]));
		}
	}

	private String readLine()
	{
		return this.console != null
			? this.console.readLine().trim()
			: this.scanner.nextLine();
	}
}
