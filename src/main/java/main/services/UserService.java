package main.services;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import main.answers.LoadLine;
import main.answers.StatusLoad;
import main.dto.PhoneWithCode;
import main.dto.UserDTO;
import main.dto.UserData;
import main.model.user.User;
import main.model.user.UserRepository;
import main.model.user.adress.PostAdress;
import main.model.user.adress.PostAdressRepository;
import main.model.user.phone.Phone;
import main.model.user.phone.PhonesRepository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PhonesRepository phonesRepository;
    private final PostAdressRepository postAdressRepository;
    private final SmsService smsService;

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

    public int sendSms(String phone){
        Phone phoneFromDB = phonesRepository.findByPhone(normalizePhone(phone));

        if (phoneFromDB == null){
            return 400;
        }
        //TODO Разкомментировать после завершения работ, сейчас сделано, чтобы коды не тратили смс пакет
//        int code = new Random().nextInt(1000,10000);
//
//        boolean sended = smsService.sendSms(phoneFromDB.getPhone(), Integer.toString(code));
//
//        if (sended){
//            phoneFromDB.setLastSendCode(code);
//            phoneFromDB.setDateOfSendCode(LocalDateTime.now());
//            phonesRepository.save(phoneFromDB);
//        }

        return 200;
    }

    public String autor(PhoneWithCode phoneWithCode){
        Phone phone = phonesRepository.findByPhone(normalizePhone(phoneWithCode.getPhone()));

        if (phone == null){
            return "";
        }

        if (equalsHash(phoneWithCode.getCode(), String.valueOf(phone.getLastSendCode()))){
            return phone.getUser().getId();
        }

        return "";
    }

    public boolean equalsHash(String hash, String code){
        MessageDigest digest = null;
        try{
            digest = MessageDigest.getInstance("SHA-256");
        }catch (Exception e){
            return false;
        }

        String year = LocalDateTime.now().getYear() + "";
        String month = LocalDateTime.now().getMonth().name();
        String day = LocalDateTime.now().getDayOfYear() + "";

        String resultSecretCode = code + year + month + day;

        byte[] hashArray = HexFormat.of().parseHex(hash);

        byte[] secretCodeArray = digest.digest(resultSecretCode.getBytes());

        return MessageDigest.isEqual(secretCodeArray, hashArray);
    }

    private String normalizePhone(String phone){
        String phoneWithoutAppostrof = phone.replace("\"","");

        String phoneForFound = null;

        if (phoneWithoutAppostrof.substring(0,1).equals("8")){
            phoneForFound = "7" + phoneWithoutAppostrof.substring(1);
        }else {
            phoneForFound = phoneWithoutAppostrof;
        }

        return phoneForFound;
    }

    public UserData getUserData(String id){
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()){
            UserData userData = new UserData();
            userData.setName(user.get().getName());
            userData.setPhones(user.get().getPhones().stream().map(Phone::getPhone).toList());
            userData.setAddresses(user.get().getPostAdresses().stream().map(PostAdress::getPostAdress).toList());
            return userData;
        }

        return null;
    }
}
