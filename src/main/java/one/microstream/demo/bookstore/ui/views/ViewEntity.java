package one.microstream.demo.bookstore.ui.views;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.function.ValueProvider;

import one.microstream.demo.bookstore.data.Named;
import one.microstream.demo.bookstore.ui.data.BookStoreDataProvider;

public abstract class ViewEntity<E> extends VerticalLayout
{
	final Grid<E>                  grid;
	final List<FilterField<E, ?>>  filterFields;
	BookStoreDataProvider<E> dataProvider;

	protected ViewEntity()
	{
		super();

		this.grid = new Grid<>();
		this.grid.setMultiSort(true);

		this.filterFields = new ArrayList<>();

		this.setSizeFull();

		this.addAttachListener(event ->
		{
			this.createUI();
			this.add(this.grid);

			this.dataProvider = BookStoreDataProvider.New(this.backend());
			this.updateFilter();
			this.grid.setDataProvider(this.dataProvider);
		});
	}

	protected abstract void createUI();

	protected abstract BookStoreDataProvider.Backend<E> backend();

	protected final void refresh()
	{
		this.dataProvider.refreshAll();
	}

	protected final void updateFilter()
	{
		SerializablePredicate<E> filter = entity -> true;
		for(final FilterField<E, ?> filterField : this.filterFields)
		{
			filter = filterField.filter(filter);
		}
		this.dataProvider.setFilter(filter);

		this.filterFields.forEach(this::refreshIfDynamic);
	}

	protected Grid.Column<E> addGridColumn(final ValueProvider<E, ?> valueProvider)
	{
		return this.grid.addColumn(valueProvider)
			.setResizable(true)
			.setSortable(true);
	}

	@SuppressWarnings("unchecked")
	protected Grid.Column<E> addGridColumn(
		final ValueProvider<E, ?> valueProvider,
		final Component headerComponent
	)
	{
		if(headerComponent instanceof FilterField)
		{
			this.filterFields.add((FilterField<E, ?>)headerComponent);
		}

		return this.addGridColumn(valueProvider)
			.setHeader(headerComponent);
	}

	protected Grid.Column<E> addGridColumn(
		final ValueProvider<E, ?> valueProvider,
		final String labelText
	)
	{
		return this.addGridColumn(valueProvider)
			.setHeader(labelText);
	}

	protected Grid.Column<E> addGridColumnWithTextFilter(
		final String title,
		final ValueProvider<E, String> valueProvider
	)
	{
		final FilterTextField<E> text = new FilterTextField<>(
			title,
			value -> entity -> StringUtils.containsIgnoreCase(valueProvider.apply(entity), value)
		);
		text.addValueChangeListener(event -> this.updateFilter());
		return this.addGridColumn(
			valueProvider,
			text
		);
	}

	protected <F extends Named> Grid.Column<E> addGridColumnWithDynamicFilter(
		final String title,
		final ValueProvider<E, F> valueProvider
	)
	{
		return this.addGridColumnWithDynamicFilter(title, valueProvider, null);
	}

	protected <F extends Named> Grid.Column<E> addGridColumnWithDynamicFilter(
		final String title,
		final ValueProvider<E, F> valueProvider,
		final F preselectedValue
	)
	{
		final FilterComboBox<E, F> combo = new FilterComboBox<>(
			title,
			value -> entity -> valueProvider.apply(entity) == value
		);
		combo.setDataProvider(new AbstractBackEndDataProvider<F, String>()
		{
			@Override
			protected Stream<F> fetchFromBackEnd(final Query<F, String> query)
			{
				Stream<F> stream = ViewEntity.this.dataProvider.fetch(new Query<>())
					.map(valueProvider)
					.distinct();
				final Optional<String> filter = query.getFilter();
				if(filter.isPresent())
				{
					final String filterText = filter.get().trim();
					if(filterText.length() > 0)
					{
						stream = stream.filter(entity ->
							StringUtils.containsIgnoreCase(entity.name(), filterText)
						);
					}
				}
				final Comparator<F> comparator = query.getInMemorySorting();
				if(comparator != null)
				{
					stream = stream.sorted(comparator);
				}
				return stream
					.skip(query.getOffset())
					.limit(query.getLimit());
			}

			@Override
			protected int sizeInBackEnd(final Query<F, String> query)
			{
				return (int)this.fetchFromBackEnd(query).count();
			}

			@Override
			public boolean isInMemory()
			{
				return ViewEntity.this.dataProvider.isInMemory();
			}
		});
		this.markAsDynamic(combo);

		if(preselectedValue != null)
		{
			combo.setValue(preselectedValue);
		}

		combo.addValueChangeListener(event -> this.updateFilter());

		return this.addGridColumn(
			entity -> valueProvider.apply(entity).name(),
			combo
		);
	}

	private void markAsDynamic(final ComboBox<?> combo)
	{
		ComponentUtil.setData(
			combo,
			"dynamicDataProvider",
			combo.getDataProvider()
		);
	}

	@SuppressWarnings("rawtypes")
	private void refreshIfDynamic(final Object element)
	{
		if(element instanceof Component)
		{
			final DataProvider dataProvider = (DataProvider)ComponentUtil.getData(
				(Component)element,
				"dynamicDataProvider"
			);
			if(dataProvider != null)
			{
				dataProvider.refreshAll();
			}
		}
	}

	protected E getSelectedEntity()
	{
		return this.grid.getSelectionModel().getFirstSelectedItem().orElse(null);
	}
}
