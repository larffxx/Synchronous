package com.larffxx.synchronoustelegram.service.registry;

import com.larffxx.synchronoustelegram.service.controller.ui.buttons.Button;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;


@Component
@AllArgsConstructor
public class ButtonRegistry implements Registry<Button> {
    private final Collection<Button> buttons;
    private Map<String, Button> buttonMap;

    @PostConstruct
    public void mapCommands() {
        for (Button button : buttons) {
            buttonMap.put(button.getButton(), button);
        }

    }

    public Button get(String button) {
        return buttonMap.get(button);
    }
}

