package edu.volkov.events.views.practice.low_coupling;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import edu.volkov.events.data.entity.Person;
import edu.volkov.events.data.service.PersonService;
import edu.volkov.events.views.MainLayout;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static com.vaadin.flow.component.grid.GridVariant.LUMO_NO_BORDER;
import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_START;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_SUCCESS;

@RequiredArgsConstructor
@PageTitle("Low Coupling")
@Route(value = "low-coupling/:personID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class LowCouplingView extends Div implements BeforeEnterObserver {

    private final String PERSON_ID = "personID";
    private final String PERSON_EDIT_ROUTE_TEMPLATE = "low-coupling/%d/edit";

    private Grid<Person> grid;

    private final GoodPersonForm personForm;
    private final PersonService personService;

    private Registration saveRegistration;
    private Registration cancelRegistration;

    @PostConstruct
    private void initView() {
        addClassNames("low-coupling-view", "flex", "flex-col", "h-full");

        configGrid();

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        createGridLayout(splitLayout);
        add(splitLayout);
        splitLayout.addToSecondary(personForm);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        saveRegistration = ComponentUtil.addListener(UI.getCurrent(), GoodPersonForm.SaveEvent.class, saveEvent -> {
            Person formObject = saveEvent.getFormObject();
            personService.update(formObject);
            refreshGrid();
            Notification.show("Person details stored.").addThemeVariants(LUMO_SUCCESS);
            UI.getCurrent().navigate(LowCouplingView.class);
        });
        cancelRegistration = ComponentUtil.addListener(UI.getCurrent(), GoodPersonForm.CancelEvent.class, cancelEvent -> {
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

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> personId = event.getRouteParameters().getInteger(PERSON_ID);
        if (personId.isPresent()) {
            Optional<Person> samplePersonFromBackend = personService.get(personId.get());
            if (samplePersonFromBackend.isPresent()) {
                // personForm.populateForm(samplePersonFromBackend.get()); //<---
                fireSelectEventToUI(samplePersonFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested person was not found, ID = %d", personId.get()), 3000, BOTTOM_START);
                refreshGrid();
                event.forwardTo(LowCouplingView.class);
            }
        }
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
                fireSelectEventToUI(null);
                UI.getCurrent().navigate(LowCouplingView.class);
            }
        });
    }

    private void fireSelectEventToUI(Person selected) {
        ComponentUtil.fireEvent(UI.getCurrent(), new SelectEvent(this, selected));
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    //ON_SELECT
    public static class SelectEvent extends ComponentEvent<LowCouplingView> {

        @Getter
        private final Person selected;

        public SelectEvent(LowCouplingView source, Person selected) {
            super(source, false);
            this.selected = selected;
        }
    }
}
