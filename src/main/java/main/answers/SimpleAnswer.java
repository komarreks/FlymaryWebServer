package main.answers;

import lombok.Getter;

@Getter
public class SimpleAnswer {
    String text;
    boolean isError;

    public SimpleAnswer(boolean isError, String text){
        this.isError = isError;
        this.text = text;
    }
}
