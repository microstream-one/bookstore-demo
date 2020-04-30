package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "purchases", layout = RootLayout.class)
@SuppressWarnings("serial")
public class ViewPurchases extends VerticalLayout
{
	public ViewPurchases()
	{
		super();

		this.add(new H3("Purchases"));
	}
}
