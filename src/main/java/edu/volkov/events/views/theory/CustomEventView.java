package edu.volkov.events.views.theory;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import edu.volkov.events.views.MainLayout;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PageTitle("Custom Event")
@Route(value = "custom-event", layout = MainLayout.class)
public class CustomEventView extends GeneralView {

    private final CustomButton customBtn = new CustomButton();

    @Override
    protected void configListeners() {
        //Notifier's implementation
        customBtn.setText("I listen to everything");
        customBtn.addKeyPressListener(keyPressEvent -> {
            String keyName = String.join(", ", keyPressEvent.getKey().getKeys());
            customBtn.setText(keyName.toUpperCase());
        });

        //Custom event, fired from server
        customBtn.addTextChangeListener(textChangeEvent -> {
            String btnText = textChangeEvent.getBtnText();
            Notification
                    .show("Event from Server. Button text changed to: " + btnText)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        //Custom event, fired from client
        customBtn.addDomClickListener(domClickEvent -> {
            String tagName = domClickEvent.getTagName();
            Notification
                    .show("Event from Client. The event source is " + tagName, 5000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        });
    }

    @Override
    protected <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return super.addListener(eventType, listener);
    }

    @Override
    protected void addContent() {
        add(new HorizontalLayout(customBtn));
    }

    //Use Notifier's
    public static class CustomButton extends Button implements KeyNotifier {

        @Override
        public void setText(String btnText) {
            super.setText(btnText);
            //Fire custom event
            fireEvent(new TextChangeEvent(this, false, btnText));
        }

        public Registration addTextChangeListener(ComponentEventListener<TextChangeEvent> listener) {
            return addListener(TextChangeEvent.class, listener);
        }

        public Registration addDomClickListener(ComponentEventListener<CustomDomClickEvent> listener) {
            return addListener(CustomDomClickEvent.class, listener);
        }
    }

    //Custom event
    public static class TextChangeEvent extends ComponentEvent<CustomButton> {

        @Getter
        private final String btnText;

        public TextChangeEvent(CustomButton source, boolean fromClient, String btnText) {
            super(source, fromClient);
            this.btnText = btnText;
        }
    }

    //Custom client event
    @DomEvent("click")
    public static class CustomDomClickEvent extends ComponentEvent<CustomButton> {

        @Getter
        private final String tagName;

        public CustomDomClickEvent(CustomButton source,
                                   boolean fromClient,
                                   @EventData("event.target.tagName") String tagName) {
            super(source, fromClient);
            this.tagName = tagName;
        }
    }
}
