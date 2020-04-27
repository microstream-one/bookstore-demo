
package one.microstream.demo.bookstore;

import java.util.Locale;


public interface Language extends Named
{
	public Locale locale();
	
	public static Language New(
		final Locale locale
	)
	{
		return new Default(locale);
	}
	
	public static class Default implements Language
	{
		private final Locale locale;
		
		public Default(
			final Locale locale
		)
		{
			super();
			this.locale = locale;
		}
		
		@Override
		public Locale locale()
		{
			return this.locale;
		}
		
		@Override
		public String name()
		{
			return this.locale.getDisplayLanguage();
		}
		
		@Override
		public String toString()
		{
			return "Language [" + this.name() + "]";
		}
		
	}
	
}
