package one.microstream.demo.bookstore.ui.views;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import one.microstream.demo.bookstore.BookStoreDemo;
import one.microstream.demo.bookstore.data.Shop;
import one.microstream.demo.bookstore.ui.data.BookStoreDataProvider.Backend;

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
			"Show Inventory",
			VaadinIcon.STOCK.create(),
			event -> this.showInventory(this.getSelectedEntity())
		);
		showInventoryButton.setEnabled(false);
		this.grid.addSelectionListener(event -> {
			final boolean b = event.getFirstSelectedItem().isPresent();
			showInventoryButton.setEnabled(b);
		});

		this.add(new HorizontalLayout(showInventoryButton));
	}

	private void showInventory(final Shop shop)
	{
		final Map<String, String> params = new HashMap<>();
		params.put("shop", shop.name());
		this.getUI().get().navigate("inventory", QueryParameters.simple(params));
	}

	@Override
	protected Backend<Shop> backend()
	{
		return BookStoreDemo.getInstance().data().shops()::compute;
	}

}
