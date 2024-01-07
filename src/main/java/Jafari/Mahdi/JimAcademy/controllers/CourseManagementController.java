package Jafari.Mahdi.JimAcademy.controllers;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/course")
public class CourseManagementController {

    final HttpSession session;
    final UserRepository userRepository;
    final StudentRepository studentRepository;
    final TeacherRepository teacherRepository;
    final CourseRepository courseRepository;

    public CourseManagementController(HttpSession session, UserRepository userRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, CourseRepository courseRepository) {
        this.session = session;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/{classNumber}")
    public ModelAndView init(@PathVariable Long classNumber,@ModelAttribute("map") ModelMap map){
        map.put("currentCourse" , courseRepository.findById(classNumber).get());
        return returnUserValidationModel("مدیریت کلاس", "courseManagement", map);
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
