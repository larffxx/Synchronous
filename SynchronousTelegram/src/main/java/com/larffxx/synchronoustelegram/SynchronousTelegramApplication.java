package com.larffxx.synchronoustelegram;

import com.larffxx.synchronoustelegram.bot.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
public class SynchronousTelegramApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SynchronousTelegramApplication.class, args);
		try {
			TelegramBotsLongPollingApplication botApp = new TelegramBotsLongPollingApplication();
			botApp.registerBot(context.getBean("telegramBot", TelegramBot.class).getBotToken(), context.getBean("telegramBot", TelegramBot.class));
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

}
