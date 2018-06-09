package ru.mozlab.example.reverseword.domain;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Word {
    private @Id @Getter @Setter String word;
    private @Getter @Setter Long count=1L;

    public Word(String word) {
        this.word = word;
    }
}