package edu.volkov.events.views.theory;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import javax.annotation.PostConstruct;

public abstract class GeneralView extends VerticalLayout {

    public GeneralView() {
        setMargin(true);
        System.out.println("Create new instance of "+ this.getClass().getSimpleName());
    }

    @PostConstruct
    public void initView() {
        configListeners();
        addContent();
    }

    protected abstract void configListeners();

    protected abstract void addContent();
}
