package Jafari.Mahdi.JimAcademy.services;

import Jafari.Mahdi.JimAcademy.entities.User;
import Jafari.Mahdi.JimAcademy.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionEvaluator {

    final HttpSession session;
    final UserRepository userRepository;

    public PermissionEvaluator(HttpSession session, UserRepository userRepository) {
        this.session = session;
        this.userRepository = userRepository;
    }

    public boolean isValidUser() {
        User sessionUser = ((User) session.getAttribute("currentUser"));
        Optional<User> user = userRepository.findById(Long.valueOf(sessionUser.getId()));

        if (user.isPresent())
            if (user.get().getUsername().equals(sessionUser.getUsername()) && user.get().getPassword().equals(sessionUser.getPassword())) {
                return true;
            }
        return false;
    }
}
