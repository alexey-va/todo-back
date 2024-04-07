package ru.alexeyva.todoback.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.toggle.AbilityToggle;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

@Component
@Profile({"telegram"})
public class AnnounceBot extends AbilityBot {

    final MessageHandler messageHandler;
    final RedisTemplate<String, String> redisTemplate;

    protected AnnounceBot(Environment env, RedisTemplate<String, String> redisTemplate) {
        super(env.getProperty("TELEGRAM_BOT_TOKEN"), "todo-announce-bot");
        this.redisTemplate = redisTemplate;
        this.messageHandler = new MessageHandler(this.silent, this.db(), this.redisTemplate);
    }

    @Override
    public long creatorId() {
        return 12323523145L;
    }

    public Ability startBot() {
        return Ability.builder()
                .name("start")
                .info("Начать получать уведомления")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> messageHandler.replyToStart(ctx.chatId()))
                .build();
    }

    public Ability help() {
        return Ability.builder()
                .name("help")
                .info("Помощь")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> messageHandler.replyToHelp(ctx.chatId()))
                .build();
    }

    public Ability admin() {
        return Ability.builder()
                .name("admin")
                .info("Добавить чат в список администраторских")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> messageHandler.replyToAdmin(ctx.chatId()))
                .build();
    }

    public void sendAdminAnnouncement(String message) {
        messageHandler.sendAdminAnnouncement(message);
    }



}
