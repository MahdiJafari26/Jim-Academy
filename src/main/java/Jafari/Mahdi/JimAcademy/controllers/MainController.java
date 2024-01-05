package Jafari.Mahdi.JimAcademy.controllers;

import Jafari.Mahdi.JimAcademy.carriers.WebToast;
import Jafari.Mahdi.JimAcademy.entities.Course;
import Jafari.Mahdi.JimAcademy.entities.Student;
import Jafari.Mahdi.JimAcademy.entities.Teacher;
import Jafari.Mahdi.JimAcademy.entities.User;
import Jafari.Mahdi.JimAcademy.repositories.CourseRepository;
import Jafari.Mahdi.JimAcademy.repositories.StudentRepository;
import Jafari.Mahdi.JimAcademy.repositories.TeacherRepository;
import Jafari.Mahdi.JimAcademy.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {

    final UserRepository userRepository;
    final StudentRepository studentRepository;
    final TeacherRepository teacherRepository;
    final CourseRepository courseRepository;

    public MainController(StudentRepository studentRepository, UserRepository userRepository, TeacherRepository teacherRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/login")
    public ModelAndView init(@ModelAttribute("map") ModelMap map, @ModelAttribute User user) {
        map.put("title", "ورود");
        initialCreation();

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


    public void initialCreation() {
        Boolean adminExist = false;
        for (User tmpUser : userRepository.findAll()) {
            if (tmpUser.getUsername().equals("admin")) adminExist = true;
        }
        if (!adminExist) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setInformation("ادمین سیستم");
            admin.setName("مهدی");
            admin.setLastName("جعفری");
            userRepository.save(admin);
        }

        Boolean studentExist = false;
        for (Student tmpUser : studentRepository.findAll()) {
            if (tmpUser.getUsername().equals("mahdi")) studentExist = true;
        }
        if (!studentExist) {
            Student mahdi = new Student();
            mahdi.setUsername("mahdi");
            mahdi.setPassword("mahdi");
            mahdi.setInformation("دانشجوی ورودی 98");
            mahdi.setName("مهدیه");
            mahdi.setLastName("جعفریان");
            studentRepository.save(mahdi);
        }


        Teacher drJamshidi = new Teacher();
        ;
        Boolean drJamshidiExist = false;
        for (Teacher tmpUser : teacherRepository.findAll()) {
            if (tmpUser.getUsername().equals("jamshidi")) {
                drJamshidiExist = true;
                drJamshidi = tmpUser;
            }
        }
        if (!drJamshidiExist) {
            drJamshidi.setUsername("jamshidi");
            drJamshidi.setPassword("jamshidi");
            drJamshidi.setInformation("هیئت علمی دانشکده مهندسی کامپیوتر");
            drJamshidi.setName("دکتر کمال");
            drJamshidi.setLastName("جمشیدی");
            drJamshidi = teacherRepository.save(drJamshidi);
        }


        Boolean cyberExist = false;
        for (Course course : courseRepository.findAll()) {
            if (course.getId() == 0) cyberExist = true;
        }
        if (!cyberExist) {
            Course cyber = new Course();
            cyber.setId(0);
            cyber.setTeacher(drJamshidi);
            cyber.setInformation("آزمایشگاه سایبر فیزیک");
            courseRepository.save(cyber);
        }
    }
}
