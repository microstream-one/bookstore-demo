package one.microstream.demo.bookstore.ui.views;

import java.util.HashMap;
import java.util.Map;

import com.flowingcode.vaadin.addons.ironicons.IronIcons;
import com.flowingcode.vaadin.addons.ironicons.SocialIcons;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * Root layout for all views, containing the main menu.
 *
 */
@Push
@Theme(value = Lumo.class, variant = Lumo.DARK)
@CssImport("./styles/shared-styles.css")
@SuppressWarnings("serial")
public class RootLayout
	extends AppLayoutRouterLayout<LeftLayouts.LeftResponsive>
	implements PageConfigurator
{
	public RootLayout()
	{
		super();

		final Label titleLabel = new Label(this.getTranslation("app.title"));
		titleLabel.getStyle().set("margin-right", "var(--lumo-space-xl)");

		final HorizontalLayout logoLayout = new HorizontalLayout(
			new Image("frontend/images/logo32.png", "Logo"),
			titleLabel
		);
		logoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

		this.init(
			AppLayoutBuilder
			.get(LeftLayouts.LeftResponsive.class)
			.withIconComponent(logoLayout)
			.withAppMenu(
				LeftAppMenuBuilder.get()
				.add(new LeftNavigationItem(this.getTranslation("home")     , VaadinIcon.HOME.create()          , ViewMain.class     ))
				.add(new LeftNavigationItem(this.getTranslation("books")    , VaadinIcon.BOOK.create()          , ViewBooks.class    ))
				.add(new LeftNavigationItem(this.getTranslation("shops")    , VaadinIcon.SHOP.create()          , ViewShops.class    ))
				.add(new LeftNavigationItem(this.getTranslation("inventory"), VaadinIcon.STORAGE.create()       , ViewInventory.class))
				.add(new LeftNavigationItem(this.getTranslation("customers"), SocialIcons.PERSON.create()       , ViewCustomers.class))
				.add(new LeftNavigationItem(this.getTranslation("purchases"), IronIcons.SHOPPING_BASKET.create(), ViewPurchases.class))
				.build()
			)
			.build()
		);
	}

	@Override
	public void configurePage(
		final InitialPageSettings settings
	)
	{
		addLink(settings, "frontend/images/favicon.svg", "rel", "icon", "type", "image/svg+xml");
		addLink(settings, "frontend/images/favicon.ico", "rel", "alternate icon");
	}

	private static void addLink(
		final InitialPageSettings settings,
		final String href,
		final String... attributes
	)
	{
		final Map<String, String> map = new HashMap<>();
		for(int i = 0; i < attributes.length; )
		{
			map.put(attributes[i++], attributes[i++]);
		}
		settings.addLink(href, map);
	}

}
