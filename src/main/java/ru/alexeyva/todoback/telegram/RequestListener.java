package ru.alexeyva.todoback.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.alexeyva.todoback.events.RequestEvent;
import ru.alexeyva.todoback.services.GeoIPService;

@Component
@RequiredArgsConstructor
@Profile("telegram")
public class RequestListener {

    final AnnounceBot announceBot;
    final GeoIPService geoIPService;

    @EventListener
    public void handleRequestEvent(RequestEvent event) {
        GeoIPService.GeoLocation location = geoIPService.getLocation(event.getIp());
        String city = location == null ? "Unknown" : location.getLocation().getValue();
        announceBot.sendAdminAnnouncement("Request from "+event.getIp()+" ("+city+"): "+event.getRequestedObject());
    }

}
