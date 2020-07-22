
package one.microstream.demo.bookstore.graphql;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeRuntimeWiring;


/**
 * GraphQL provider component, which creates and wires GraphQL schema to the data layer.
 *
 */
@Component
public class GraphqlProvider
{
	@Autowired
	GraphqlDataFetchers dataFetchers;
	GraphQL             graphql     ;

	public GraphqlProvider()
	{
		super();
	}

	@Bean
	public GraphQL graphql()
	{
		return this.graphql;
	}

	@PostConstruct
	public void init() throws IOException
	{
		final String sdl = Resources.toString(
			Resources.getResource("META-INF/resources/graphql/schema.graphqls"),
			Charsets.UTF_8
		);
		final GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(
			new SchemaParser().parse(sdl),
			this.buildWiring()
		);
		this.graphql = GraphQL.newGraphQL(schema).build();
	}

	private RuntimeWiring buildWiring()
	{
		return RuntimeWiring.newRuntimeWiring()
			.wiringFactory(new BookStoreWiringFactory())
			.type(this.buildQueryType())
			.build()
		;
	}

	/**
	 * Wires data fetchers for all public methods of {@link GraphqlDataFetchers}.
	 */
	private TypeRuntimeWiring.Builder buildQueryType()
	{
		final TypeRuntimeWiring.Builder builder = newTypeWiring("Query");
		for(final Method method : this.dataFetchers.getClass().getDeclaredMethods())
		{
			if(Modifier.isPublic(method.getModifiers())
			&& DataFetcher.class.isAssignableFrom(method.getReturnType()))
			{
				try
				{
					builder.dataFetcher(
						method.getName(),
						(DataFetcher<?>)method.invoke(this.dataFetchers)
					);
				}
				catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		return builder;
	}

}
