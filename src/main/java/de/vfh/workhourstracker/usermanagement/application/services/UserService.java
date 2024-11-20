package de.vfh.workhourstracker.usermanagement.application.services;

import de.vfh.workhourstracker.usermanagement.domain.model.User;
import de.vfh.workhourstracker.usermanagement.infrastructure.repositories.UserRepository;
import de.vfh.workhourstracker.shared.util.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    EventLogger eventLogger = new EventLogger();

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

    //region validation
    public String validateName(String name) {
        if (name == null || name.isEmpty()) {
            eventLogger.logError("Name darf nicht leer sein.");
            return null;
        }
        if (name.length() > 64) {
            eventLogger.logError("Name darf nicht länger als 64 Zeichen sein.");
            return null;
        }

        String namePattern = "^[a-zA-ZäöüÄÖÜß\\s'-]{1,155}$";
        if (!name.matches(namePattern)) {
            eventLogger.logWarning("Name ist nicht valide.");
            return null;
        }
        return name;
    }

    public String validateMailAddress(String mailAddress) {
        //TODO
        if (mailAddress == null || mailAddress.isEmpty()) {
            eventLogger.logError("E-Mail-Addresse darf nicht leer sein.");
            return null;

        }
        if (mailAddress.length() >= 64) {
            eventLogger.logError("E-Mail-Addresse darf nicht länger als 64 Zeichen sein.");
            return null;
        }

        String mailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!mailAddress.matches(mailPattern)) {
            eventLogger.logWarning("Mailadresse ist nicht valide.");
            return null;
        }
        return mailAddress;
    }
    //endregion
}
