package main.answers;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StatusLoadUsers {
    private boolean status;
    private String error;
    private List<String> loadedUsers = new ArrayList<>();

    public void addLoadingUser(String id1c){
        loadedUsers.add(id1c);
    }
}
