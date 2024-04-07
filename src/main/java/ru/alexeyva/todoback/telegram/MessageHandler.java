package ru.alexeyva.todoback.telegram;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Set;
import java.util.stream.Collectors;

public class MessageHandler {

    private final SilentSender silentSender;
    private final Set<Long> adminChats;
    private final RedisTemplate<String, String> redisTemplate;


    public MessageHandler(SilentSender silentSender, DBContext db, @Autowired(required = false) RedisTemplate<String, String> redisTemplate) {
        this.silentSender = silentSender;
        this.redisTemplate = redisTemplate;
        if (redisTemplate != null) {
            this.adminChats = redisTemplate.opsForSet().members("todolist.adminChats").stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toSet());
        } else {
            this.adminChats = db.getSet("adminChats");
        }
    }


    public void replyToStart(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Hello! I am AnnounceBot. I can help you with your tasks. Type /help to see the list of available commands.");
        silentSender.execute(sendMessage);
    }

    public void replyToHelp(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Here is the list of available commands:\n" +
                        "/help - show this message\n" +
                        "/start - start the conversation\n" +
                        //"/add - add a new task\n" +
                        //"/list - show the list of tasks\n" +
                        "/admin - add a chat to the list of admin chats\n"
                //"/done - mark a task as done\n" +
                //"/delete - delete a task\n" +
                //"/clear - delete all tasks\n" +
                //"/cancel - cancel the current operation"
        );
        silentSender.execute(sendMessage);
    }


    public void replyToAdmin(Long aLong) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(aLong);
        if (addAdminChat(aLong)) sendMessage.setText("Chat added to the list of admin chats");
        else sendMessage.setText("Chat is already in the list of admin chats");

        silentSender.execute(sendMessage);
    }

    public void sendAdminAnnouncement(String message) {
        for (Long chatId : adminChats) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(message);
            silentSender.execute(sendMessage);
        }
    }

    private boolean addAdminChat(long chatId) {
        if (adminChats.contains(chatId)) return false;
        if (redisTemplate != null) {
            redisTemplate.opsForSet().add("todolist.adminChats", String.valueOf(chatId));
        }
        adminChats.add(chatId);
        return true;
    }
}
