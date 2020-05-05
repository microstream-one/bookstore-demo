package one.microstream.demo.bookstore.util;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.function.DefaultMonetarySummaryStatistics;
import org.javamoney.moneta.function.MonetarySummaryStatistics;

/**
 * Various collection utilities.
 *
 */
public interface CollectionUtils
{
	/**
	 * Returns a {@link Stream} of the given collection
	 * or an empty {@link Stream} if the collection is <code>null</code>.
	 * @param <T>
	 * @param <C>
	 * @param collection a collection or <code>null</code>
	 * @return a {@link Stream} backed by the collection or an empty one
	 */
	public static <T, C extends Collection<T>> Stream<T> ensureStream(
		final C collection
	)
	{
		return collection == null
			? Stream.empty()
			: collection.stream();
	}

	/**
	 * Returns a parallel {@link Stream} of the given collection
	 * or an empty {@link Stream} if the collection is <code>null</code>.
	 * @param <T>
	 * @param <C>
	 * @param collection a collection or <code>null</code>
	 * @return a parallel {@link Stream} backed by the collection or an empty one
	 */
	public static <T, C extends Collection<T>> Stream<T> ensureParallelStream(
		final C collection
	)
	{
		return collection == null
			? Stream.empty()
			: collection.parallelStream();
	}

	/**
	 * Computes the maximum key of a {@link Map} based on the {@link Comparable} values.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @param <M>
	 * @param map
	 * @return the maximum key
	 */
	public static <K, V extends Comparable<V>, M extends Map<K, V>> K maxKey(
		final M map
	)
	{
		final Entry<K, V> max = map.entrySet()
			.parallelStream()
			.max((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
			.orElse(null);
		return max != null
			? max.getKey()
			: null;
	}

	/**
	 * Creates a {@code Collector} that produces the sum of a {@link MonetaryAmount}
     * function applied to the input elements.  If no elements are present,
     * the result is {@link MonetaryAmount} of zero.
     *
	 * @param <T> the type of the input elements
	 * @param currencyUnit the used currency unit
	 * @param mapper a function extracting the property to be summed
	 * @return a {@code Collector} that produces the sum of a derived property
	 */
	public static <T> Collector<T, ?, MonetaryAmount> summingMonetaryAmount(
		final CurrencyUnit currencyUnit,
		final Function<? super T, MonetaryAmount> mapper
	)
	{
		return Collector.of(
			() -> DefaultMonetarySummaryStatistics.of(currencyUnit),
			(stats, elem) -> stats.accept(mapper.apply(elem)),
			MonetarySummaryStatistics::combine,
			MonetarySummaryStatistics::getSum
		);
	}
}
