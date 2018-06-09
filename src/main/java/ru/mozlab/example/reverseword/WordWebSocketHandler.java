package ru.mozlab.example.reverseword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import ru.mozlab.example.reverseword.repository.WordRepository;

@Component
public class WordWebSocketHandler implements WebSocketHandler {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired	WordRepository repo;

    private final String words2json() {
        String jsonOut = "";
        try {
             jsonOut = om.writeValueAsString(repo.findAll());
         } catch (JsonProcessingException jp) {}         
         return jsonOut;
    }

    private final String save2repo(String w) {
        if (!w.isEmpty()) repo.save(w);
        return w;
    }

    @Override
    public Mono<Void> handle(WebSocketSession sess) {
        return sess.send(sess.receive()
            .map(WebSocketMessage::getPayloadAsText)
            .map(w->new StringBuilder(w).reverse().toString())
            .map(w->save2repo(w))
            .map(w->words2json())
            .map(sess::textMessage));
    }
}
