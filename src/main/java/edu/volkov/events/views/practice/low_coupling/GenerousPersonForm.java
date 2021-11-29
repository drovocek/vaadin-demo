package edu.volkov.events.views.practice.low_coupling;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import edu.volkov.events.views.practice.low_coupling.events.CancelPublisher;
import edu.volkov.events.views.practice.low_coupling.events.SavePublisher;
import edu.volkov.events.views.practice.low_coupling.events.SelectNotifier;

import javax.annotation.PostConstruct;

@UIScope
@SpringComponent
public class GenerousPersonForm extends Div implements SelectNotifier, SavePublisher, CancelPublisher {

    private TextField firstName;
    private TextField lastName;
    private TextField email;

    private Button cancel;
    private Button save;

    private BeanValidationBinder<Person> binder;
    private Person formObject;

    private Registration selectRegistration;

    @PostConstruct
    private void initForm() {
        configButtons();
        configDiv();
        configBinder();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        selectRegistration = addSelectListener(UI.getCurrent(), selectEvent -> {
            Person selected = (Person) selectEvent.getSelected();
            if (selected == null) {
                clearForm();
            } else {
                populateForm(selected);
            }
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
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
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
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
            fireCancelEventTo(UI.getCurrent());
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
                fireSaveEventTo(UI.getCurrent(), this.formObject);
                //----
                clearForm();
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the person details.");
            }
        });
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Person value) {
        this.formObject = value;
        binder.readBean(this.formObject);
    }
}
