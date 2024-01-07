package Jafari.Mahdi.JimAcademy.controllers;

import Jafari.Mahdi.JimAcademy.carriers.WebToast;
import Jafari.Mahdi.JimAcademy.entities.Course;
import Jafari.Mahdi.JimAcademy.entities.Homework;
import Jafari.Mahdi.JimAcademy.entities.Student;
import Jafari.Mahdi.JimAcademy.entities.User;
import Jafari.Mahdi.JimAcademy.repositories.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseManagementController {

    final HttpSession session;
    final UserRepository userRepository;
    final StudentRepository studentRepository;
    final TeacherRepository teacherRepository;
    final CourseRepository courseRepository;
    final HomeworkRepository homeworkRepository;

    public CourseManagementController(HttpSession session, UserRepository userRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, CourseRepository courseRepository, HomeworkRepository homeworkRepository) {
        this.session = session;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.homeworkRepository = homeworkRepository;
    }

    @GetMapping("/{classNumber}")
    public ModelAndView init(@PathVariable Long classNumber, @ModelAttribute("map") ModelMap map, @ModelAttribute Homework homework){
        Course currentCourse = courseRepository.findById(classNumber).get();
        map.put("currentCourse" , currentCourse);
        session.setAttribute("currentCourse" , currentCourse);
        return returnUserValidationModel("مدیریت کلاس", "courseManagement", map);
    }

    @PostMapping("/createNewHomework")
    public RedirectView createNewHomework(@ModelAttribute Homework homework, RedirectAttributes attributes) {
        ModelMap map = new ModelMap();
        try {
            Course currentCourse = (Course) session.getAttribute("currentCourse");
            List<Homework> homeworkList = currentCourse.getHomeworkList();
            homeworkList.add(homework);
            currentCourse.setHomeworkList(homeworkList);
            homework.setCourse(currentCourse);
            homeworkRepository.save(homework);
            courseRepository.save(currentCourse);
        } catch (Exception e) {
            map.put("toast", new WebToast(WebToast.ToastType.ERROR_TYPE.getValue(), "مشکلی در ثبت پست به وجود آمده است"));
            attributes.addFlashAttribute("map", map);
            return new RedirectView("/dashboard");
        }
        map.put("toast", new WebToast(WebToast.ToastType.CONFIRMATION_TYPE.getValue(), "ثبت پست شما با موفقیت انجام شد"));
        attributes.addFlashAttribute("map", map);
        return new RedirectView("/dashboard");

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
