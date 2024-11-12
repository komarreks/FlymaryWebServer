package main.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.StatusLoad;
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

    //region FIELDS
    private final UserService service;
    //endregion

    //region METHODS
    /**
     * Метод возращает DTO список всех пользователей
     * @return
     */
    @GetMapping(value = "/getAllUsers/")
    public ResponseEntity getUsers(){
        List<String> userList = service.getAllDTO();

        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    /**
     * Метод для загрузки / обновления пользователей
     * @param userList
     * @return
     */
    @PostMapping(value = "/loadAllUsers", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity loadUsers(@RequestBody ArrayNode userList){
        StatusLoad statusLoad = service.loadUsers(userList);

        return ResponseEntity.status(HttpStatus.CREATED).body(statusLoad);
    }
    //endregion
}
