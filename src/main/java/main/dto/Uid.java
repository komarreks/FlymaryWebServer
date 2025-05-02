package main.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class Uid {
    private String uid;

    public Uid(String uid) {
        this.uid = uid;
    }

    public Uid(){
        this.uid = "";
    }
}
