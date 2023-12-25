package Jafari.Mahdi.JimAcademy.controllers;

import Jafari.Mahdi.JimAcademy.MotionDetector;
import Jafari.Mahdi.JimAcademy.carriers.WebToast;
import Jafari.Mahdi.JimAcademy.entities.Student;
import Jafari.Mahdi.JimAcademy.entities.User;
import Jafari.Mahdi.JimAcademy.repositories.StudentRepository;
import Jafari.Mahdi.JimAcademy.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.opencv.core.Core;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class MainController {

    final UserRepository userRepository;
    final StudentRepository studentRepository;

    public MainController(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public ModelAndView init(@ModelAttribute("map") ModelMap map, @ModelAttribute User user) {
        map.put("title", "ورود");
        Thread thread = new Thread(() -> new MotionDetector(0));
        thread.start();
        return new ModelAndView("login", map);
    }

    @PostMapping("/login")
    public RedirectView login(@ModelAttribute User user, RedirectAttributes attributes, HttpSession session) {
        ModelMap map = new ModelMap();
        List<User> userList = userRepository.findAll();
        for (User tmpUser : userList) {
            if (user.getUsername().equals(tmpUser.getUsername()) && user.getPassword().equals(tmpUser.getPassword())) {
                session.setAttribute("currentUser", tmpUser);
                return new RedirectView("dashboard");
            }
        }
        map.put("toast", new WebToast(WebToast.ToastType.ERROR_TYPE.getValue(), "نام کاریری یا رمز عبور صحیح نیست"));
        attributes.addFlashAttribute("map", map);
        return new RedirectView("login");

    }

    @GetMapping("/signup")
    public ModelAndView signupForm(@ModelAttribute("map") ModelMap map, @ModelAttribute Student student) {
        map.put("title", "ثبت نام");
        return new ModelAndView("signup", map);

    }

    @PostMapping("/signup")
    public RedirectView signupSubmit(@ModelAttribute Student student, RedirectAttributes attributes) {
        ModelMap map = new ModelMap();
        try {
            studentRepository.save(student);
        } catch (Exception e) {
            map.put("toast", new WebToast(WebToast.ToastType.ERROR_TYPE.getValue(), "مشکلی در ثبت نام به وجود آمده است"));
            attributes.addFlashAttribute("map", map);
            return new RedirectView("signup");
        }
        map.put("toast", new WebToast(WebToast.ToastType.CONFIRMATION_TYPE.getValue(), "ثبت نام شما با موفقیت انجام شد"));
        attributes.addFlashAttribute("map", map);
        return new RedirectView("login");

    }
}
