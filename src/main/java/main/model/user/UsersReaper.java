package main.model.user;

import lombok.Getter;
import lombok.Setter;
import main.model.user.User;
import main.model.user.adress.PostAdress;
import main.model.user.adress.PostAdressRepository;
import main.model.user.phone.Phone;
import main.model.user.phone.PhonesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
@Component
public class UsersReaper {
    @Autowired
    private PhonesRepository phonesRepository;
    @Autowired
    private PostAdressRepository postAdressRepository;

    public UsersReaper(PhonesRepository phonesRepository, PostAdressRepository postAdressRepository){
        this.phonesRepository = phonesRepository;
        this.postAdressRepository = postAdressRepository;
    }

    public boolean phonesAnalysis(User user, List<String> phones){

        AtomicBoolean userModified = new AtomicBoolean(false);

        phones.forEach(phone ->{
            Phone phoneFromDB = phonesRepository.findByPhone(phone);

            boolean modified = false;

            if (phoneFromDB == null) {
                phoneFromDB = new Phone(user, phone);
                modified = true;
            }

            if (!phoneFromDB.user.getId1c().equals(user.getId1c())) {
                phoneFromDB.setUser(user);
                modified = true;
            }

            if (!user.getId1c().equals(phoneFromDB.user.getId1c())){
                phoneFromDB.setUser(user);
                modified = true;
            }

            if (modified){
                phonesRepository.save(phoneFromDB);
                userModified.set(true);
            }

        });

        boolean isDeletedPhones = clearDeletedPhones(user,phones);

        return userModified.get() || isDeletedPhones;
    }

    private boolean clearDeletedPhones(User user, List<String> phones){
        List<Phone> phonesFromDB = phonesRepository.findByUser(user);

        AtomicBoolean modified = new AtomicBoolean(false);

        if (phonesFromDB != null){
            phonesFromDB.forEach(phoneFDB ->{
                String phoneAsString = phoneFDB.getPhone();

                if (!phones.contains(phoneAsString)){
                    phonesRepository.delete(phoneFDB);
                    user.deletePhone(phoneFDB);
                    modified.set(true);
                }

            });
        }
        return modified.get();
    }

    public boolean postAdressesAnalysis(User user, List<String> postAdresses){

        AtomicBoolean userModified = new AtomicBoolean(false);

        postAdresses.forEach(postAdress ->{
            PostAdress postAdressFDB = postAdressRepository.findByPostAdress(postAdress);

            boolean modified = false;

            if (postAdressFDB == null) {
                postAdressFDB = new PostAdress(user, postAdress);
                modified = true;
            }

            if (!postAdressFDB.getUser().getId1c().equals(user.getId1c())) {
                postAdressFDB.setUser(user);
                modified = true;
            }

            if (!user.getId1c().equals(postAdressFDB.getUser().getId1c())){
                postAdressFDB.setUser(user);
                modified = true;
            }

            if (modified){
                postAdressRepository.save(postAdressFDB);
                userModified.set(true);
            }

        });

        boolean isDeletedAdresses = clearDeletedPostAdresses(user,postAdresses);

        return userModified.get() || isDeletedAdresses;
    }

    private boolean clearDeletedPostAdresses(User user, List<String> postAdresses) {
        List<PostAdress> postAdressFDB = postAdressRepository.findByUser(user);

        AtomicBoolean modified = new AtomicBoolean(false);

        if (postAdressFDB != null) {
            postAdressFDB.forEach(adressFDB -> {
                String adressAsString = adressFDB.getPostAdress();

                if (!postAdresses.contains(adressAsString)) {
                    postAdressRepository.delete(adressFDB);
                    user.deletePostAdress(adressFDB);
                    modified.set(true);
                }

            });
        }
        return modified.get();
    }
}
