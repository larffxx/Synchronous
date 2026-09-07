package com.larffxx.synchronoustelegram.service.registry;

import com.larffxx.synchronoustelegram.service.controller.ui.buttons.Button;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Registry that looks up inline buttons by identifier.
 */
@Component
public class ButtonRegistry implements Registry<Button> {
    /**
     * All button beans discovered by Spring.
     */
    private final Collection<Button> buttons;
    /**
     * Button lookup map keyed by button identifier.
     */
    private final Map<String, Button> buttonMap = new HashMap<>();

    /**
     * Creates a button registry with the discovered buttons.
     *
     * @param buttons all button beans to register
     */
    public ButtonRegistry(Collection<Button> buttons) {
        this.buttons = buttons;
    }

    /**
     * Indexes all buttons by their identifier after construction.
     */
    @PostConstruct
    public void mapButtons() {
        for (Button button : buttons) {
            buttonMap.put(button.getButton(), button);
        }
    }

    /**
     * Returns the button registered under the given identifier.
     *
     * @param button button identifier to look up
     * @return matching button or null if none is registered
     */
    @Override
    public Button get(String button) {
        return buttonMap.get(button);
    }
}
