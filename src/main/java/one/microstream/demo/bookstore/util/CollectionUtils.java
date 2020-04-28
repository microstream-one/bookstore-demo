package one.microstream.demo.bookstore.util;

import java.util.function.Function;
import java.util.stream.Collector;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.function.DefaultMonetarySummaryStatistics;
import org.javamoney.moneta.function.MonetarySummaryStatistics;


public final class CollectionUtils
{
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
