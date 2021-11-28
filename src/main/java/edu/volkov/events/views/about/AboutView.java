package edu.volkov.events.views.about;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
import edu.volkov.events.views.MainLayout;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

    public AboutView() {
        setSpacing(false);

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        add(new H2("Listening to Vaadin"));
        add(new Paragraph("Sources of information 🤗"));
        add(new Anchor("https://refactoring.guru/ru/design-patterns/observer", "Observer pattern"));
        add(new Anchor("https://vaadin.com/docs/v8/framework/architecture/architecture-events", "Events and Listeners"));
        add(new Anchor("https://vaadin.com/docs/latest/flow/application/events", "Realisation variants"));
        add(new Anchor("https://vaadin.com/docs/v14/flow/element-api/tutorial-event-listener", "Element API realisations"));
        add(new Anchor("https://cookbook.vaadin.com/ui-eventbus", "Communicate between components attached to one UI"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
