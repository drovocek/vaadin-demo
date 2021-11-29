package edu.volkov.events.views.practice.low_coupling;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import edu.volkov.events.data.entity.Person;
import edu.volkov.events.data.service.PersonService;
import edu.volkov.events.views.practice.low_coupling.events.CancelNotifier;
import edu.volkov.events.views.practice.low_coupling.events.SaveNotifier;
import edu.volkov.events.views.practice.low_coupling.events.SelectPublisher;
import lombok.RequiredArgsConstructor;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static com.vaadin.flow.component.grid.GridVariant.LUMO_NO_BORDER;
import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_START;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_SUCCESS;

@RequiredArgsConstructor
@UIScope
@SpringComponent
public class GenerousGridView extends Div implements BeforeEnterObserver, SaveNotifier, CancelNotifier, SelectPublisher {

    private final String PERSON_ID = "personID";
    private final String PERSON_EDIT_ROUTE_TEMPLATE = "low-coupling/%d/edit";

    private final PersonService personService;

    private Grid<Person> grid;
    private Registration saveRegistration;
    private Registration cancelRegistration;

    @PostConstruct
    private void initView() {
        configGrid();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> personId = event.getRouteParameters().getInteger(PERSON_ID);
        if (personId.isPresent()) {
            Optional<Person> personFromBackend = personService.get(personId.get());
            if (personFromBackend.isPresent()) {
                // personForm.populateForm(samplePersonFromBackend.get()); //<---
                fireSelectEventTo(UI.getCurrent(), personFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested person was not found, ID = %d", personId.get()), 3000, BOTTOM_START);
                refreshGrid();
                event.forwardTo(LowCouplingView.class);
            }
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        saveRegistration = addSaveListener(UI.getCurrent(), saveEvent -> {
            Person formObject = (Person) saveEvent.getPersistent();
            personService.update(formObject);
            refreshGrid();
            Notification.show("Person details stored.").addThemeVariants(LUMO_SUCCESS);
            UI.getCurrent().navigate(LowCouplingView.class);
        });
        cancelRegistration = addCancelListener(UI.getCurrent(), cancelEvent -> {
            refreshGrid();
            Notification.show("Cancel was call.").addThemeVariants(LUMO_PRIMARY);
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        saveRegistration.remove();
        cancelRegistration.remove();
    }

    private void configGrid() {
        grid = new Grid<>(Person.class);

        grid.setDataProvider(new CrudServiceDataProvider<>(personService));
        grid.addThemeVariants(LUMO_NO_BORDER);
        grid.setHeightFull();

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
//                personForm.clearForm(); //<---
                fireSelectEventTo(UI.getCurrent(), null);
                UI.getCurrent().navigate(LowCouplingView.class);
            }
        });

        this.setId("grid-wrapper");
        this.setWidthFull();

        add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }
}
