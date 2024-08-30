package main.answers;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoadLine {
    private String id1c;
    private String status;
    private LocalDateTime date;

    public LoadLine(String id1c) {
        this.id1c = id1c;
        date = LocalDateTime.now();
    }
}
