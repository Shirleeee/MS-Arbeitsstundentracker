package de.vfh.workhourstracker.usermanagement.application.services;

import de.vfh.workhourstracker.usermanagement.domain.user.MailAddress;
import de.vfh.workhourstracker.usermanagement.domain.user.User;
import de.vfh.workhourstracker.usermanagement.domain.user.UserName;
import de.vfh.workhourstracker.usermanagement.domain.user.events.UserCreated;
import de.vfh.workhourstracker.usermanagement.infrastructure.repositories.UserRepository;
import de.vfh.workhourstracker.shared.util.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;

    @Autowired
    public UserService(ApplicationEventPublisher eventPublisher, UserRepository userRepository) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
    }

    public User createUser(String name, String mailAddress) {
        String validName = validateName(name);
        String validMailAddress = validateMailAddress(mailAddress);

        if (validName == null || validMailAddress == null) {
            EventLogger.logError("User could not be created because of invalid input");
            return null;
        }

        User user = new User(new UserName(validName), new MailAddress(validMailAddress));
        user = userRepository.save(user);

        UserCreated event = new UserCreated(this, user.getId(), user.getName(), user.getMailAddress());
        eventPublisher.publishEvent(event);

        return user;
    }

    //region validation
    public String validateName(String name) {
        if (name == null || name.isEmpty()) {
            EventLogger.logWarning("Name darf nicht leer sein.");
            return null;
        }
        if (name.length() > 64) {
            EventLogger.logWarning("Name darf nicht länger als 64 Zeichen sein.");
            return null;
        }

        String namePattern = "^[a-zA-ZäöüÄÖÜß\\s'-]{1,155}$";
        if (!name.matches(namePattern)) {
            EventLogger.logWarning("Name ist nicht valide.");
            return null;
        }
        return name;
    }

    public String validateMailAddress(String mailAddress) {
        if (mailAddress == null || mailAddress.isEmpty()) {
            EventLogger.logWarning("E-Mail-Adresse darf nicht leer sein.");
            return null;

        }
        if (mailAddress.length() >= 64) {
            EventLogger.logWarning("E-Mail-Adresse darf nicht länger als 64 Zeichen sein.");
            return null;
        }

        String mailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!mailAddress.matches(mailPattern)) {
            EventLogger.logWarning("Mailadresse ist nicht valide.");
            return null;
        }
        return mailAddress;
    }
    //endregion
}
