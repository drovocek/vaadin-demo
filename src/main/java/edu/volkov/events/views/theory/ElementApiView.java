package edu.volkov.events.views.theory;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.volkov.events.views.MainLayout;
import elemental.json.JsonObject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PageTitle("Element Api")
@Route(value = "element-api", layout = MainLayout.class)
public class ElementApiView extends GeneralView {

    private final Button elementBtn;
    private final Button elementBtnWithParams;

    @Override
    protected void configListeners() {
        //Handle browser events
        elementBtn.getElement().addEventListener("click", new DomEventListener() {

            int count = 0;

            @Override
            public void handleEvent(DomEvent domEvent) {
                Element response = ElementFactory.createDiv("My number is " + (++count));
                getElement().appendChild(response);
            }
        });

        elementBtnWithParams.getElement()
                .addEventListener("click", this::handleClick)
                .addEventData("event.shiftKey")
                .addEventData("element.offsetWidth");
    }

    private void handleClick(DomEvent event) {
        JsonObject eventData = event.getEventData();

        boolean shiftKey = eventData.getBoolean("event.shiftKey");
        double width = eventData.getNumber("element.offsetWidth");

        String text = "Shift " + (shiftKey ? "down" : "up");
        text += " on button whose width is " + width + "px";

        Element response = ElementFactory.createDiv(text);
        getElement().appendChild(response);
    }

    @Override
    protected void addContent() {
        add(new HorizontalLayout(elementBtn, elementBtnWithParams));
    }
}
