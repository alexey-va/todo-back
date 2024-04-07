package ru.alexeyva.todoback.events;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter @Setter
public class RequestEvent extends ApplicationEvent {
    String request;
    public RequestEvent(String request) {
        super(request);
        this.request = request;
    }
}
