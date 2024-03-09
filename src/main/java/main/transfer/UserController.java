package main.transfer;

import com.fasterxml.jackson.databind.node.ArrayNode;
import main.answers.StatusLoadUsers;
import main.model.user.User;
import main.model.user.UserRepository;
import main.model.user.UsersReaper;
import main.model.user.adress.PostAdressRepository;
import main.model.user.phone.PhonesRepository;
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
    @Autowired
    private PhonesRepository phonesRepository;
    @Autowired
    private PostAdressRepository postAdressRepository;

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

        UsersReaper usersReaper = new UsersReaper(phonesRepository, postAdressRepository);

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

            ArrayNode phonesArrayNode = (ArrayNode) userNode.get("phones");
            List<String> phonesList = new ArrayList<>();
            phonesArrayNode.forEach(phoneNode -> {
                String phone = phoneNode.asText();
                phonesList.add(phone);
            });

            modified = usersReaper.phonesAnalysis(user, phonesList);

            ArrayNode adressesArrayNode = (ArrayNode) userNode.get("postAdresses");
            List<String> adressesList = new ArrayList<>();
            adressesArrayNode.forEach(adressNode -> {
                String adress = adressNode.asText();
                adressesList.add(adress);
            });

            modified = usersReaper.postAdressesAnalysis(user, adressesList);

            if (modified){
                userRepository.save(user);
            }

            statusLoadUsers.addLoadingUser(id1c);
        });
//

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoadUsers);
    }
}
