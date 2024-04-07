package ru.alexeyva.todoback.events;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter @Setter
public class RequestEvent extends ApplicationEvent {
    String ip;
    String requestedObject;
    public RequestEvent(String ip, String requestedObject) {
        super(ip);
        this.ip = ip;
        this.requestedObject = requestedObject;
    }
}
