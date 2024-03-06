package main.transfer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import main.answers.StatusLoadUsers;
import main.model.User;
import main.model.UserRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("users/getAllUsers")
    public List<User> getUsers(){
        Iterable<User> userIterable = userRepository.findAll();
        List<User> userList = new ArrayList<>();

        userIterable.forEach(user -> {
            userList.add(user);
        });

        return userList;
    }

    @PostMapping(value = "users/loadAllUsers", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity loadUsers(@RequestBody ArrayNode userList){
        StatusLoadUsers statusLoadUsers = new StatusLoadUsers();
        statusLoadUsers.setStatus(true);
        statusLoadUsers.setError("");

        int countUsers = userList.size() - 1;

        userList.forEach(userNode -> {
            String id1c = String.valueOf(userNode.get("id1c"));

            boolean modified = false;

            User user = userRepository.findById1c(id1c);

            if (user == null){
                user = new User();
                user.setId1c(id1c);

                modified = true;
            }

            String name = String.valueOf(userNode.get("name"));

            if (!user.getName().equals(name)){
                user.setName(name);
                modified = true;
            }

            if (modified){
                userRepository.save(user);
            }

            statusLoadUsers.addLoadingUser(id1c);
        });
//

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoadUsers);
    }
}
