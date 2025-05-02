package main.api;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.StatusLoad;
import main.dto.PhoneWithCode;
import main.dto.Uid;
import main.dto.UserData;
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

    /**
     * Отправляет по смс код авторизации
     * @param phone
     * @return
     */
    @PostMapping("/getcode")
    public ResponseEntity getCode(@RequestBody String phone){
        int answer = service.sendSms(phone);

        return ResponseEntity.status(HttpStatus.OK).body(answer);
    }

    /**
     * Передает на клиент id пользователя для получения дальнейших данных
     * @param phoneWithCode
     * @return
     */
    @PostMapping("/autor")
    public ResponseEntity autor(@RequestBody PhoneWithCode phoneWithCode){

        String id = service.autor(phoneWithCode);

        return ResponseEntity.status(HttpStatus.OK).body(new Uid(id));
    }

    /**
     * Передает данные пользователя
     * @param userId
     * @return
     */
    @PostMapping("/userdata")
    public ResponseEntity getUserdata(@RequestBody Uid userId){
        UserData userData = service.getUserData(userId.getUid());

        if(userData == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(userData);
    }
    //endregion
}
