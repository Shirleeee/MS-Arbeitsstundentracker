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
    EventLogger eventLogger = new EventLogger();

    @Autowired
    public UserService(ApplicationEventPublisher eventPublisher, UserRepository userRepository) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
    }

    public User createUser(String name, String mailAddress) {
        String validName = validateName(name);
        String validMailAddress = validateMailAddress(mailAddress);

        if (validName == null || validMailAddress == null) {
            eventLogger.logError("User could not be created because of invalid input");
            return null;
        }

        User user = new User(new UserName(validName), new MailAddress(validMailAddress));
        user = userRepository.save(user);

        UserCreated event = new UserCreated(this, user.getId(), user.getName(), user.getMailAddress());
        eventPublisher.publishEvent(event);

        return user;
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
