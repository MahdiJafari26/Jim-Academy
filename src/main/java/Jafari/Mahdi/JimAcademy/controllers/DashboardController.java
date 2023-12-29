package Jafari.Mahdi.JimAcademy.controllers;

import Jafari.Mahdi.JimAcademy.entities.Course;
import Jafari.Mahdi.JimAcademy.entities.User;
import Jafari.Mahdi.JimAcademy.repositories.StudentRepository;
import Jafari.Mahdi.JimAcademy.repositories.TeacherRepository;
import Jafari.Mahdi.JimAcademy.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/dashboard")
@PreAuthorize("@permissionEvaluator.isValidUser()")
public class DashboardController {

    final HttpSession session;
    final UserRepository userRepository;
    final StudentRepository studentRepository;
    final TeacherRepository teacherRepository;

    public DashboardController(HttpSession session, UserRepository userRepository, StudentRepository studentRepository, TeacherRepository teacheRepository) {
        this.session = session;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacheRepository;
    }

    @GetMapping
    public ModelAndView init(@ModelAttribute("map") ModelMap map) {
        return returnUserValidationModel("داشبورد", "dashboard", map);
    }

    @GetMapping("management")
    public ModelAndView goToManagementPage(@ModelAttribute("map") ModelMap map, @ModelAttribute Course course) {
        map.put("users", userRepository.findAll());
        map.put("students", userRepository.findAll());
        map.put("teachers", userRepository.findAll());
        return returnUserValidationModel("مدیریت", "management", map);
    }

    public ModelAndView returnUserValidationModel(String title, String viewName, ModelMap map) {
        User sessionUser = ((User) session.getAttribute("currentUser"));
        if (sessionUser != null) for (User tmpUser : userRepository.findAll()) {
            if (sessionUser.getId() == tmpUser.getId() && sessionUser.getUsername().equals(tmpUser.getUsername()) && sessionUser.getPassword().equals(tmpUser.getPassword())) {
                map.put("title", title);
                map.put("user", tmpUser);
                if (tmpUser.getUsername().equals("admin")) map.put("isAdmin", true);
                else map.put("isAdmin", false);
                return new ModelAndView(viewName, map);
            }
        }
        return new ModelAndView("redirect:/login", map);
    }
}
