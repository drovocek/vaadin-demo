package edu.volkov.events.views.config;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ComponentFabric {

    @Scope("prototype")
    @Bean
    Button pushMeButton() {
        return new Button("Push me!");
    }

    @Scope("prototype")
    @Bean
    TextField realisationType() {
        TextField realisationType = new TextField("Realisation type");
        realisationType.setReadOnly(true);
        realisationType.setWidth("35%");
        return realisationType;
    }
}
