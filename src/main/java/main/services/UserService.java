package main.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.dto.UserDTO;
import main.model.user.User;
import main.model.user.UserRepository;
import main.model.user.adress.PostAdressRepository;
import main.model.user.phone.PhonesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PhonesRepository phonesRepository;
    private final PostAdressRepository postAdressRepository;

    public List<String> getAllDTO(){
    Iterable<User> userIterable = userRepository.findAll();

    List<String> usersDTOList = new ArrayList<>();

    for (User user: userIterable) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setId1c(user.getId1c());
        userDTO.setName(user.getName());
        userDTO.setPhones(user.phonesToString());
        userDTO.setAdresses(user.adressesToString());
    }

    return usersDTOList;
    }

    public StatusLoad loadUsers(ArrayNode jsUsers){
        StatusLoad statusLoadUsers = new StatusLoad();

        jsUsers.forEach(jsUser -> {
            String id1c = jsUser.get("id1c").textValue();
            LoadLine loadLine = new LoadLine(id1c);

            User user = userRepository.findById1c(id1c);

            if (user == null){
                user = new User();
                user.setId1c(id1c);
                loadLine.setStatus("Загружен");
            }

            user.setName(jsUser.get("name").textValue());
            user.setDeleted(jsUser.get("deleted").intValue());

            phonesRepository.deleteAll(user.getPhones());
            postAdressRepository.deleteAll(user.getPostAdresses());

            user.clearPhonesAndPostAdress();

            ArrayNode phonesArrayNode = (ArrayNode) jsUser.get("phones");
            List<String> phonesList = new ArrayList<>();
            phonesArrayNode.forEach(phoneNode -> {
                String phone = phoneNode.asText();
                phonesList.add(phone);
            });

            ArrayNode adressesArrayNode = (ArrayNode) jsUser.get("postAdresses");
            List<String> adressesList = new ArrayList<>();
            adressesArrayNode.forEach(adressNode -> {
                String adress = adressNode.asText();
                adressesList.add(adress);
            });

            user.savePhonesAndPostAdress(phonesList, adressesList);

            userRepository.save(user);
            if (loadLine.getStatus() == null) loadLine.setStatus("Обновлен");

            statusLoadUsers.addLog(loadLine);
        });

        return statusLoadUsers;
    }
}
