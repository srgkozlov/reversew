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

@Service
class WordService {
    private static final ObjectMapper om = new ObjectMapper();
    @Autowired  WordRepository repo;

    public String findAllJSON() {
        String jsonOut = "";
        try {
             jsonOut = om.writeValueAsString(repo.findAll());
         } catch (JsonProcessingException jp) {}         
         return jsonOut;
    }

    public String save(String w) {
        if (!w.isEmpty()) repo.save(new StringBuilder(w).reverse().toString());
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
            .map(w->service.save(w))
            .map(w->service.findAllJSON())
            .map(sess::textMessage));
    }
}
