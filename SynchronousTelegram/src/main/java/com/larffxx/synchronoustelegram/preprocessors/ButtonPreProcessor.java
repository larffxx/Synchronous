package com.larffxx.synchronoustelegram.preprocessors;

import com.larffxx.synchronoustelegram.buttons.Button;
import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;


@Component
public class ButtonPreProcessor implements PreProcessor<Button> {
    private final Collection<Button> buttons;
    private Map<String, Button> buttonMap;

    @PostConstruct
    public void mapCommands() {
        for (Button button : buttons) {
            buttonMap.put(button.getButton(), button);
        }

    }

    public Button getCommand(String button) {
        return buttonMap.get(button);
    }

    public Button getCommand(UpdateReceiver updateReceiver) {
        return buttonMap.get(updateReceiver.getUpdate().getCallbackQuery().getData());
    }

    public ButtonPreProcessor(final Collection<Button> buttons, final Map<String, Button> buttonMap) {
        this.buttons = buttons;
        this.buttonMap = buttonMap;
    }
}

