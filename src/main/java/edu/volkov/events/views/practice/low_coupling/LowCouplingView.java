package edu.volkov.events.views.practice.low_coupling;

import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.volkov.events.views.MainLayout;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@PageTitle("Low Coupling")
@Route(value = "low-coupling/:personID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class LowCouplingView extends Div {

    private final GenerousPersonForm personForm;
    private final GenerousGridView gridView;

    @PostConstruct
    private void initView() {
        addClassNames("low-coupling-view", "flex", "flex-col", "h-full");

        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        add(splitLayout);

        splitLayout.addToPrimary(gridView);
        splitLayout.addToSecondary(personForm);
    }
}
