package main.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.StatusLoad;
import main.model.user.UserRepository;
import main.model.user.adress.PostAdressRepository;
import main.model.user.phone.PhonesRepository;
import main.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService service;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PhonesRepository phonesRepository;
    @Autowired
    private PostAdressRepository postAdressRepository;

    @GetMapping(value = "/getAllUsers/")
    public ResponseEntity getUsers(){
        List<String> userList = service.getAllDTO();

        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @PostMapping(value = "/loadAllUsers", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity loadUsers(@RequestBody ArrayNode userList){
        StatusLoad statusLoad = service.loadUsers(userList);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }
}
