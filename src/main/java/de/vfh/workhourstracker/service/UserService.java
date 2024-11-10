package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.entity.User;
import de.vfh.workhourstracker.repository.UserRepository;
import de.vfh.workhourstracker.util.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    EventLogger eventLogger = new EventLogger();

    public String validateName(String name) {
        //TODO
        if (name == null || name.isEmpty()) {
            return null;

        } else if (name.length() < 156) {
            return name;
        }

        String namePattern = "^[a-zA-ZäöüÄÖÜß\\s'-]{1,155}$";
        if (name.matches(namePattern)) {
            return name;
        } else {
            eventLogger.logWarning("Name ist nicht valide.");
            return null;
        }
    }

    public String validateMailAddress(String mailAddress) {
        //TODO
        if (mailAddress == null || mailAddress.isEmpty()) {
            return null;

        }
        return mailAddress;
    }
}
