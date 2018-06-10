package ru.mozlab.example.reverseword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import ru.mozlab.example.reverseword.repository.WordRepository;


class JSON {
    private static final ObjectMapper om = new ObjectMapper();
    public static final String string(Object x) {
        try {
            return om.writeValueAsString(x);
        } catch (JsonProcessingException jp) {}
        return "";
    }
}

@Service
class WordService {

    @Autowired  WordRepository repo;

    private String reverse(String w) {
        return new StringBuilder(w).reverse().toString();
    }

    public String findAll() {
        return JSON.string(repo.findAll());
    }

    public String save(String w) {
        if (!w.isEmpty()) repo.save(reverse(w));
        return w;
    }
}

@Component
public class WordWebSocketHandler implements WebSocketHandler {
    
    @Autowired
    WordService service;

    @Override
    public Mono<Void> handle(WebSocketSession sess) {
        return sess.send(sess.receive()
            .map(WebSocketMessage::getPayloadAsText)
            .map(service::save)
            .map(x -> service.findAll())
            .map(sess::textMessage));
    }
}
