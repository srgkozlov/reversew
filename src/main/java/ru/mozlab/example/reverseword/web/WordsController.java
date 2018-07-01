package ru.mozlab.example.reverseword.web;

import java.util.List;
import java.lang.StringBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import ru.mozlab.example.reverseword.repository.WordRepository;
import ru.mozlab.example.reverseword.domain.Word;

@RestController
public class WordsController {
    private Logger log = LoggerFactory.getLogger(WordsController.class);

    @Autowired
    WordRepository repo;

    @MessageMapping("/get/words")
    @GetMapping("/words")
    @SendTo("/words")
    @ResponseBody
    public List<Word> getWords() {
        return repo.findAll();
    }

    @MessageMapping("/word")
    @PostMapping("/word")
    @SendTo("/words")
    @ResponseBody
    public List<Word> addWord(@RequestBody String word) {
        repo.save(reverse(word));
        return repo.findAll();
    }

    private String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }
}



