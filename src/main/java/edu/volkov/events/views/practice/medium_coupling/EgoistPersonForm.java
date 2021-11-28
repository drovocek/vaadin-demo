package edu.volkov.events.views.practice.medium_coupling;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import edu.volkov.events.data.entity.Person;
import lombok.Getter;

import javax.annotation.PostConstruct;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY;

@UIScope
@SpringComponent
public class EgoistPersonForm extends Div {

    private TextField firstName;
    private TextField lastName;
    private TextField email;

    private Button cancel;
    private Button save;

    private BeanValidationBinder<Person> binder;
    private Person formObject;

    @PostConstruct
    private void initForm() {
        configButtons();
        configDiv();
        configBinder();
    }

    private void configDiv() {
        this.setClassName("flex flex-col");
        this.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        this.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        firstName = new TextField("First Name");
        lastName = new TextField("Last Name");
        email = new TextField("Email");
        Component[] fields = new Component[]{firstName, lastName, email};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(this);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(LUMO_TERTIARY);
        save.addThemeVariants(LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void configBinder() {
        binder = new BeanValidationBinder<>(Person.class);
        binder.bindInstanceFields(this);
    }

    private void configButtons() {
        cancel = new Button("Cancel");
        save = new Button("Save");

        cancel.addClickListener(e -> {
            clearForm();
//            refreshGrid(); <--- In MediumCouplingView
            //----
            fireEvent(new CancelEvent(this));
            //----
        });

        save.addClickListener(e -> {
            try {
                if (this.formObject == null) {
                    this.formObject = new Person();
                }
                binder.writeBean(this.formObject);
//                personService.update(this.formObject); <--- In MediumCouplingView
//                refreshGrid(); <--- In MediumCouplingView
//                Notification.show("Person details stored.");
//                UI.getCurrent().navigate(MediumCouplingView.class);
                //----
                fireEvent(new SaveEvent(this, this.formObject));
                //----
                clearForm();
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the person details.");
            }
        });
    }

    public void clearForm() {
        populateForm(null);
    }

    public void populateForm(Person value) {
        this.formObject = value;
        binder.readBean(this.formObject);
    }

    public Registration onCancel(ComponentEventListener<CancelEvent> onCancelAction) {
        //method from com.vaadin.flow.component.Component
        return super.addListener(CancelEvent.class, onCancelAction);
    }

    public Registration onSave(ComponentEventListener<SaveEvent> onSaveAction) {
        //method from com.vaadin.flow.component.Component
        return super.addListener(SaveEvent.class, onSaveAction);
    }

    //ON_CANCEL
    public static class CancelEvent extends ComponentEvent<EgoistPersonForm> {

        public CancelEvent(EgoistPersonForm source) {
            super(source, false);
        }
    }

    //ON_SAVE
    public static class SaveEvent extends ComponentEvent<EgoistPersonForm> {

        @Getter
        private final Person formObject;

        public SaveEvent(EgoistPersonForm source, Person formObject) {
            super(source, false);
            this.formObject = formObject;
        }
    }
}
