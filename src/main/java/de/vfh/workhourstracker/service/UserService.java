package de.vfh.workhourstracker.service;

import de.vfh.workhourstracker.entity.User;
import de.vfh.workhourstracker.repository.UserRepository;
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

    public String validateName(String name) {
        //TODO
        if (name == null || name.isEmpty()) {
            return null;

        } else if (name.length() < 156) {
            return name;
        }
        return name;

    }

    public String validateMailAddress(String mailAddress) {
        //TODO
        if (mailAddress == null || mailAddress.isEmpty()) {
            return null;

        }
        return mailAddress;
    }
}
