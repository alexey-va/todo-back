package ru.alexeyva.todoback.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.alexeyva.todoback.events.RequestEvent;

@Component
@RequiredArgsConstructor
@Profile("telegram")
public class RequestListener {

    final AnnounceBot announceBot;

    @EventListener
    public void handleRequestEvent(RequestEvent event) {
        announceBot.sendAdminAnnouncement(event.getRequest());
    }

}
