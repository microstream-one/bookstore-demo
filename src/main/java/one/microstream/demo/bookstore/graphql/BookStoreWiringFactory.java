package one.microstream.demo.bookstore.graphql;

import java.lang.reflect.Method;

import graphql.schema.DataFetcher;
import graphql.schema.idl.FieldWiringEnvironment;
import graphql.schema.idl.WiringFactory;

/**
 * Used to override the default GraphQL data fetcher for properties.
 * <p>
 * This one maps schema names directly to method names, instead of getter methods.
 *
 *
 */
public class BookStoreWiringFactory implements WiringFactory
{
	public BookStoreWiringFactory()
	{
		super();
	}

	@Override
	public DataFetcher<?> getDefaultDataFetcher(
		final FieldWiringEnvironment environment
	)
	{
		final String methodName = environment.getFieldDefinition().getName();
		return env ->
		{
			final Object obj    = env.getSource();
			final Method method = obj.getClass().getMethod(methodName);
			return method.invoke(obj);
		};
	}

}
