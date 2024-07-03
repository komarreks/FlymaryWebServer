package main.answers;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StatusLoad {
    private boolean status;
    private String error;
    private List<String> loaded = new ArrayList<>();

    public void addLoading(String id1c){
        loaded.add(id1c);
    }
}
