package one.microstream.demo.bookstore.ui.views;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.function.SerializableComparator;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEvent;
import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Named;
import org.apache.commons.lang3.StringUtils;

import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Abstract view to display entities in a {@link Grid}.
 *
 * @param <E> the entity type
 */
public abstract class ViewEntity<E> extends VerticalLayout
{
	final Grid<E>                 grid;
	final List<FilterField<E, ?>> filterFields;
	HeaderRow                     filterRow;
//	BookStoreDataProvider<E>      dataProvider;

	protected ViewEntity()
	{
		super();

		this.grid = createGrid();

		this.filterFields = new ArrayList<>();

		this.setSizeFull();

		this.addAttachListener(event ->
		{
			this.createUI();
			this.add(this.grid);

			listEntities();
		});
	}

	protected static <T> Grid<T> createGrid()
	{
		final Grid<T> grid = new Grid<>();
		grid.setMultiSort(true);
		grid.setColumnReorderingAllowed(true);
		grid.addThemeVariants(
			GridVariant.LUMO_NO_BORDER,
			GridVariant.LUMO_NO_ROW_BORDERS,
			GridVariant.LUMO_ROW_STRIPES
		);
		return grid;
	}


	public void listEntities() {
		grid.setItems(q -> {
			var comparators = q.getSortOrders().stream().map(so ->
					grid.getColumnByKey(so.getSorted()).getComparator(so.getDirection())
			).collect(Collectors.toList());
			return compute(stream -> {
				stream = stream.filter(getPredicate());
				for(Comparator c : comparators) {
					stream = stream.sorted(c);
				}
				return stream.skip(q.getOffset())
						.limit(q.getLimit());
			})
					/*
					 * Without following hack there seems to be an NPA thrown
					 * from somewhere from Vaadin internals, when scrolled fast
					 * on e.g. Inventory view. To be investigated...
					 */
					.collect(Collectors.toList()).stream();
		});
	}

	protected abstract void createUI();


	public abstract <R> R compute(SerializableFunction<Stream<E>, R> function);

	protected final SerializablePredicate<E> getPredicate()
	{
		SerializablePredicate<E> filter = entity -> entity != null;
		for(final FilterField<E, ?> filterField : this.filterFields)
		{
			filter = filterField.filter(filter);
		}
		return filter;
	}

	protected void gridDataUpdated()
	{
		// no-op by default
	}

	protected Grid.Column<E> addGridColumn(
		final String colKey,
		final ValueProvider<E, ?> valueProvider
	)
	{
		return addGridColumn(this.grid, colKey, valueProvider);
	}

	protected static <T> Grid.Column<T> addGridColumn(
		final Grid<T> grid,
		final String colKey,
		final ValueProvider<T, ?> valueProvider
	)
	{
		return grid.addColumn(valueProvider)
			.setHeader(grid.getTranslation(colKey))
			.setKey(colKey)
			.setResizable(true)
			.setSortable(true);
	}

	protected Grid.Column<E> addGridColumn(
		final String title,
		final Renderer<E> renderer
	)
	{
		return addGridColumn(this.grid, title, renderer);
	}

	protected static <T> Grid.Column<T> addGridColumn(
		final Grid<T> grid,
		final String title,
		final Renderer<T> renderer
	)
	{
		return grid.addColumn(renderer)
			.setHeader(title)
			.setResizable(true)
			.setSortable(true);
	}

	@SuppressWarnings("unchecked")
	protected Grid.Column<E> addGridColumn(
		final String colKey,
		final ValueProvider<E, ?> valueProvider,
		final Component filterComponent
	)
	{
		if(filterComponent instanceof FilterField)
		{
			this.filterFields.add((FilterField<E, ?>)filterComponent);
		}


		final Column<E> column = this.addGridColumn(colKey, valueProvider);

		if(filterComponent instanceof HasSize)
		{
			((HasSize)filterComponent).setSizeFull();
		}
		if(this.filterRow == null)
		{
			this.filterRow = this.grid.appendHeaderRow();
		}
		this.filterRow.getCell(column).setComponent(filterComponent);

		return column;
	}

	protected Grid.Column<E> addGridColumnWithTextFilter(
		final String colKey,
		final ValueProvider<E, String> valueProvider
	)
	{
		final FilterTextField<E> text = new FilterTextField<>(
			value -> entity -> StringUtils.containsIgnoreCase(valueProvider.apply(entity), value)
		);
		text.addValueChangeListener(event -> this.listEntities());
		return this.addGridColumn(
				colKey,
			valueProvider,
			text
		);
	}

	protected <F extends Named> FilterComboBox<E, F> addGridColumnWithDynamicFilter(
		final String title,
		final ValueProvider<E, F> valueProvider
	)
	{
		final FilterComboBox<E, F> combo = new FilterComboBox<>(
			value -> entity -> valueProvider.apply(entity) == value
		);


		combo.setItems(query -> {
			return compute(s -> s.map(valueProvider))
					.distinct()
					.filter(f -> StringUtils.containsIgnoreCase(f.name(), query.getFilter().get()))
					.skip(query.getOffset())
					.limit(query.getLimit());
		});

		combo.addValueChangeListener(event -> listEntities());

		this.addGridColumn(
			title,
			entity -> valueProvider.apply(entity).name(),
			combo
		);
		return combo;
	}

	protected E getSelectedEntity()
	{
		return this.grid.getSelectionModel()
			.getFirstSelectedItem()
			.orElse(null);
	}

	protected static <T> Renderer<T> moneyRenderer(
		final ValueProvider<T, ? extends MonetaryAmount> valueProvider
	)
	{
		return new TextRenderer<>(
			entity -> BookStoreDemo.MONETARY_AMOUNT_FORMAT.format(
				valueProvider.apply(entity)
			)
		);
	}

	protected static String getQueryParameter(
		final BeforeEvent event,
		final String name
	)
	{
		final List<String> list = event.getLocation().getQueryParameters().getParameters().get(name);
		return list != null && list.size() == 1
			? list.get(0)
			: null;
	}

}
