package main.answers;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StatusLoad {
    private boolean status;
    private List<String> errors = new ArrayList<>();
    private List<String> loaded = new ArrayList<>();
    private List<LocalDateTime> dates = new ArrayList<>();

    public void addLoading(String id1c){
        loaded.add(id1c);
    }

    public void addError(String error){
        errors.add(error);
    }

    public void addDate(){
        dates.add(LocalDateTime.now());
    }

    public void addLog(String id1c, String status){
        addLoading(id1c);
        addError(status);
        addDate();
    }
}
