package main.transfer;

import main.answers.StatusLoadUsers;
import main.model.User;
import main.model.UserRepository;
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
    public ResponseEntity loadUsers(@RequestBody List<User> userList){
        StatusLoadUsers statusLoadUsers = new StatusLoadUsers();
        statusLoadUsers.setStatus(true);
        statusLoadUsers.setError("");

        for(int i = 0; i<userList.size(); i++){
            User user = userRepository.findById1c(userList.get(i).getId1c());

            if (user==null){
                user = new User();
            }

            user.setId1c(userList.get(i).getId1c());
            user.setName(userList.get(i).getName());
            //user.setPhone(userList.get(i).getPhone());
            user.setPostAdress(userList.get(i).getPostAdress());

            try {
                userRepository.save(user);
            }catch (Exception ex){
                statusLoadUsers.setStatus(false);
                statusLoadUsers.setError(ex.getMessage());
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(statusLoadUsers);
            }

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoadUsers);
    }
}
