package main.transfer;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import main.answers.StatusLoad;
import main.model.user.User;
import main.model.user.UserRepository;
import main.model.user.UsersReaper;
import main.model.user.adress.PostAdressRepository;
import main.model.user.phone.PhonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhonesRepository phonesRepository;
    @Autowired
    private PostAdressRepository postAdressRepository;

    @GetMapping(value = "/getAllUsers/")
    public ResponseEntity getUsers(){
        Iterable<User> userIterable = userRepository.findAll();

        ArrayNode userList = new ArrayNode(new JsonNodeFactory(false));

        userIterable.forEach(user -> {
            ObjectNode userNode = new ObjectNode(new JsonNodeFactory(false));
            userNode.put("id1c", user.getId1c());
            userNode.put("name", user.getName());
            userNode.put("phones", user.phonesToString());
            userNode.put("adresses", user.adressesToString());
            userList.add(userNode);
        });

        return new ResponseEntity(userList,HttpStatus.OK);
    }

    @PostMapping(value = "/loadAllUsers", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity loadUsers(@RequestBody ArrayNode userList){
        StatusLoad statusLoadUsers = new StatusLoad();
        statusLoadUsers.setStatus(true);
        statusLoadUsers.setError("");

        UsersReaper usersReaper = new UsersReaper(phonesRepository, postAdressRepository);

        userList.forEach(userNode -> {
            String id1c = userNode.get("id1c").textValue();

            boolean modified = false;

            User user = userRepository.findById1c(id1c);

            int deleted = userNode.get("deleted").intValue();

            if (user == null && deleted == 0){
                user = new User();
                user.setId1c(id1c);

                modified = true;
            } else if (user != null && deleted == 1) {
                userRepository.delete(user);
            }

            String name = userNode.get("name").textValue();

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

            statusLoadUsers.addLoading(id1c);
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoadUsers);
    }
}
