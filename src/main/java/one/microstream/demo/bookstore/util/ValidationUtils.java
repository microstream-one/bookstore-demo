package one.microstream.demo.bookstore.util;

import java.util.function.Supplier;

public interface ValidationUtils
{
	public static <T extends CharSequence> T requireNonEmpty(
		final T charSequence,
		final Supplier<String> messageSupplier
	)
	{
		if(charSequence == null)
		{
			throw new NullPointerException(messageSupplier.get());
		}
		if(charSequence.length() == 0)
		{
			throw new IllegalArgumentException(messageSupplier.get());
		}
        return charSequence;
	}

	public static <T extends CharSequence> T requireNonBlank(
		final T charSequence,
		final Supplier<String> messageSupplier
	)
	{
		requireNonEmpty(charSequence, messageSupplier);
		for(int i = 0, c = charSequence.length(); i < c; i++)
		{
			if(!Character.isWhitespace(charSequence.charAt(i)))
			{
				return charSequence;
			}
		}
		throw new IllegalArgumentException(messageSupplier.get());
	}
}
