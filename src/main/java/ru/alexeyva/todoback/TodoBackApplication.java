package ru.alexeyva.todoback;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Objects;

@SpringBootApplication
@Slf4j
public class TodoBackApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(TodoBackApplication.class, args);
        if (ctx.getEnvironment().matchesProfiles("telegram")) {
            try {
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                AbilityBot announceBot = ctx.getBean("announceBot", AbilityBot.class);
                botsApi.registerBot(announceBot);
            } catch (Exception e) {
                log.error("Error starting Telegram bot", e);
            }
        }
    }

}
