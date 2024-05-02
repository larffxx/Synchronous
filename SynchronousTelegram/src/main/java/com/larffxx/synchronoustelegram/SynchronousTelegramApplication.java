package com.larffxx.synchronoustelegram;

import com.larffxx.synchronoustelegram.bot.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class SynchronousTelegramApplication {

	public static void main(String[] args) throws TelegramApiException {
		ConfigurableApplicationContext context = SpringApplication.run(SynchronousTelegramApplication.class, args);
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		TelegramBot bot = context.getBean("telegramBot", TelegramBot.class);
		botsApi.registerBot(bot);
	}

}
