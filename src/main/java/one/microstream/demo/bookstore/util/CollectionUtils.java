package one.microstream.demo.bookstore.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.function.DefaultMonetarySummaryStatistics;
import org.javamoney.moneta.function.MonetarySummaryStatistics;


public final class CollectionUtils
{
	public static <T, C extends Collection<T>> Stream<T> ensureStream(
		final C collection
	)
	{
		return collection == null
			? Stream.empty()
			: collection.stream();
	}

	public static <T, C extends Collection<T>> Stream<T> ensureParallelStream(
		final C collection
	)
	{
		return collection == null
			? Stream.empty()
			: collection.parallelStream();
	}

	public static <K, V extends Comparable<V>, M extends Map<K, V>> K maxKey(
		final M map
	)
	{
		return map.entrySet()
			.parallelStream()
			.max((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
			.get()
			.getKey();
	}

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


	private CollectionUtils()
	{
		throw new Error();
	}

}
