package main.model;

import lombok.Getter;
import lombok.Setter;
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

    public UsersReaper(PhonesRepository phonesRepository){
        this.phonesRepository = phonesRepository;
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
}
