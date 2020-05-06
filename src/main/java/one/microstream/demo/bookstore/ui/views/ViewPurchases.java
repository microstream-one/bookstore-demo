package one.microstream.demo.bookstore.ui.views;

import static one.microstream.demo.bookstore.BookStoreDemo.monetaryAmountFormat;
import static org.javamoney.moneta.function.MonetaryFunctions.summarizingMonetary;

import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.javamoney.moneta.function.MonetarySummaryStatistics;

import com.google.common.collect.Range;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Customer;
import one.microstream.demo.bookstore.data.Purchase;
import one.microstream.demo.bookstore.data.Purchase.Item;
import one.microstream.demo.bookstore.data.Purchases;
import one.microstream.demo.bookstore.data.Shop;
import one.microstream.demo.bookstore.ui.data.BookStoreDataProvider.Backend;

/**
 * View to display {@link Purchases}.
 *
 */
@Route(value = "purchases", layout = RootLayout.class)
@SuppressWarnings("serial")
public class ViewPurchases extends ViewEntity<Purchase> implements HasUrlParameter<String>
{
	int      year = Year.now().getValue();
	Shop     shop;
	Customer customer;
	Label    totalColumnFooter;

	public ViewPurchases()
	{
		super();
	}

	@Override
	public void setParameter(
		final BeforeEvent event,
		@OptionalParameter final String parameter)
	{
		final Map<String, List<String>> params = event.getLocation().getQueryParameters().getParameters();
		final List<String> shopParams = params.get("shop");
		if(shopParams != null && shopParams.size() == 1)
		{
			this.shop = BookStoreDemo.getInstance().data().shops().ofName(shopParams.get(0));
		}
		final List<String> customerParams = params.get("customer");
		if(customerParams != null && customerParams.size() == 1)
		{
			try
			{
				final int customerId = Integer.parseInt(customerParams.get(0));
				this.customer = BookStoreDemo.getInstance().data().customers().ofId(customerId);
			}
			catch(final NumberFormatException e)
			{
			}
		}
	}

	@Override
	protected void createUI()
	{
		this.addGridColumnWithDynamicFilter(this.getTranslation("shop")     , Purchase::shop     , this.shop    );
		this.addGridColumnWithDynamicFilter(this.getTranslation("employee") , Purchase::employee                );
		this.addGridColumnWithDynamicFilter(this.getTranslation("customer") , Purchase::customer , this.customer);
		this.addGridColumn                 (this.getTranslation("timestamp"), Purchase::timestamp               );
		this.addGridColumn                 (this.getTranslation("total")    , moneyRenderer(Purchase::total)    )
			.setFooter(this.totalColumnFooter = new Label());

		final Range<Integer> years = BookStoreDemo.getInstance().data().purchases().years();

		final IntegerField yearField = new IntegerField();
		yearField.setHasControls(true);
		yearField.setMin(years.lowerEndpoint());
		yearField.setMax(years.upperEndpoint());
		yearField.setValue(this.year);
		yearField.addValueChangeListener(event -> {
			this.year = event.getValue();
			this.refresh();
		});

		final HorizontalLayout bar = new HorizontalLayout(
			new Label(this.getTranslation("year")),
			yearField
		);
		bar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
		this.add(bar);

		this.grid.setItemDetailsRenderer(new ComponentRenderer<>(this::createPurchaseDetails));
		this.grid.setDetailsVisibleOnClick(true);
	}

	private Component createPurchaseDetails(final Purchase purchase)
	{
		final Grid<Purchase.Item> grid = this.createGrid();
		addGridColumn(grid, this.getTranslation("isbn13")   , item -> item.book().isbn13()           );
		addGridColumn(grid, this.getTranslation("book")     , item -> item.book().title()            );
		addGridColumn(grid, this.getTranslation("author")   , item -> item.book().author().name()    );
		addGridColumn(grid, this.getTranslation("publisher"), item -> item.book().publisher().name() );
		addGridColumn(grid, this.getTranslation("price")    , moneyRenderer(Item::price)             );
		addGridColumn(grid, this.getTranslation("amount")   , Item::amount                           );
		addGridColumn(grid, this.getTranslation("total")    , moneyRenderer(Item::itemTotal)         );
		grid.setDataProvider(DataProvider.fromStream(purchase.items()));
		grid.setHeightByRows(true);
		return grid;
	}

	@Override
	protected Backend<Purchase> backend()
	{
		return this::compute;
	}

	private <R> R compute(final Function<Stream<Purchase>, R> function)
	{
		return BookStoreDemo.getInstance().data().purchases().computeByYear(
			this.year,
			function
		);
	}

	@Override
	protected void gridDataUpdated()
	{
		final MonetarySummaryStatistics stats = this.dataProvider.fetch(new Query<>())
			.map(Purchase::total)
			.collect(summarizingMonetary(BookStoreDemo.currencyUnit()));
		this.totalColumnFooter.setText(
			monetaryAmountFormat().format(stats.getSum())
		);
	}

}
