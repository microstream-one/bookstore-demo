package one.microstream.demo.bookstore.ui.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route(value = "", layout = RootLayout.class)
public class ViewMain extends VerticalLayout
{
	public ViewMain()
	{
		super();

		final Image image = new Image("frontend/images/bookstoredemo.svg", "BookStore Demo");
		image.setWidth("80%");
		image.setMaxWidth("800px");
		image.setHeight(null);

		this.add(image);
		this.setSizeFull();
		this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		this.setJustifyContentMode(JustifyContentMode.CENTER);
	}

}
