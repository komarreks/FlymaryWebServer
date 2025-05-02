package main.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserData {
    String name;
    List<String> phones;
    List<String> addresses;

    public UserData() {
        phones = new ArrayList<>();
        addresses = new ArrayList<>();
    }
}
