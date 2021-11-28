package edu.volkov.events.views.theory;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.volkov.events.views.MainLayout;
import lombok.RequiredArgsConstructor;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.button.ButtonVariant.LUMO_SUCCESS;

@RequiredArgsConstructor
@PageTitle("Simple")
@Route(value = "simple", layout = MainLayout.class)
public class SimpleView extends GeneralView {

    private final Button lambdaBtn;
    private final Button anonymousClassBtn;
    private final Button implementationBtn;
    private final Button pushPullBtn;
    private final Button pullPushBtn;

    @Override
    protected void configListeners() {
        //Lambda Expressions
        lambdaBtn.addClickListener(clickEvent -> clickEvent.getSource().setText("Satisfaction"));

        //Anonymous Class
        anonymousClassBtn.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {

            int count = 0;

            @Override
            public void onComponentEvent(ClickEvent<Button> event) {
                event.getSource().setText("Satisfaction " + (++count) + " times");
            }
        });

        //Implementing an Event Handler
        implementationBtn.addClickListener(new MyClickListener()); //<----

        //Method reference
        pushPullBtn.setText("Pull");
        pullPushBtn.setText("Push");
        pushPullBtn.addThemeVariants(LUMO_SUCCESS);
        pullPushBtn.addThemeVariants(LUMO_PRIMARY);
        pushPullBtn.addClickListener(this::switchBtnType);
        pullPushBtn.addClickListener(this::switchBtnType);
    }

    //Interaction of components
    private void switchBtnType(ClickEvent<Button> clickEvent) {
        ButtonVariant[] removedThemes = {LUMO_SUCCESS, LUMO_PRIMARY};
        pushPullBtn.removeThemeVariants(removedThemes);
        pullPushBtn.removeThemeVariants(removedThemes);

        Button sourceBtn = clickEvent.getSource();
        String sourceBtnText = sourceBtn.getText();
        boolean sourceBtnTextIsPullText = "Pull".equals(sourceBtnText);
        sourceBtn.setText(sourceBtnTextIsPullText ? "Push" : "Pull");
        sourceBtn.addThemeVariants(sourceBtnTextIsPullText ? LUMO_PRIMARY : LUMO_SUCCESS);

        if (sourceBtn.equals(pullPushBtn)) {
            pushPullBtn.setText(sourceBtnTextIsPullText ? "Pull" : "Push");
            pushPullBtn.addThemeVariants(sourceBtnTextIsPullText ? LUMO_SUCCESS : LUMO_PRIMARY);
        } else {
            pullPushBtn.setText(sourceBtnTextIsPullText ? "Pull" : "Push");
            pullPushBtn.addThemeVariants(sourceBtnTextIsPullText ? LUMO_SUCCESS : LUMO_PRIMARY);
        }
    }

    static class MyClickListener implements ComponentEventListener<ClickEvent<Button>> { //<----

        int count = 0;

        @Override
        public void onComponentEvent(ClickEvent<Button> event) {
            event.getSource().setText("I count to " + (++count));
        }
    }

    @Override
    protected void addContent() {
        add(lambdaBtn, anonymousClassBtn, implementationBtn, new HorizontalLayout(pushPullBtn, pullPushBtn));
    }
}
