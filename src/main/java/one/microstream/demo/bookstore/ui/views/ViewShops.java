package one.microstream.demo.bookstore.ui.views;

import java.util.stream.Stream;

import com.flowingcode.vaadin.addons.ironicons.IronIcons;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Shop;
import one.microstream.demo.bookstore.data.Shops;

/**
 * View to display {@link Shops}.
 *
 */
@Route(value = "shops", layout = RootLayout.class)
@SuppressWarnings("serial")
public class ViewShops extends ViewNamedWithAddress<Shop>
{
	public ViewShops()
	{
		super();
	}

	@Override
	protected void createUI()
	{
		this.addGridColumnForName();
		this.addGridColumnsForAddress();

		final Button showInventoryButton = new Button(
			this.getTranslation("showInventory"),
			VaadinIcon.STOCK.create(),
			event -> this.showInventory(this.getSelectedEntity())
		);
		final Button showPurchasesButton = new Button(
			this.getTranslation("showPurchases"),
			IronIcons.SHOPPING_BASKET.create(),
			event -> this.showPurchases(this.getSelectedEntity())
		);

		showInventoryButton.setEnabled(false);
		showPurchasesButton.setEnabled(false);
		this.grid.addSelectionListener(event -> {
			final boolean b = event.getFirstSelectedItem().isPresent();
			showInventoryButton.setEnabled(b);
			showPurchasesButton.setEnabled(b);
		});

		this.add(new HorizontalLayout(showInventoryButton, showPurchasesButton));
	}

	private void showInventory(final Shop shop)
	{
		getUI().get().navigate(ViewInventory.class).get().filterBy(shop);
	}

	private void showPurchases(final Shop shop)
	{
		getUI().get().navigate(ViewPurchases.class).get().filterBy(shop);
	}

	@Override
	public <R> R compute(SerializableFunction<Stream<Shop>, R> function) {
		return BookStoreDemo.getInstance().data().shops().compute(function);
	}

}
