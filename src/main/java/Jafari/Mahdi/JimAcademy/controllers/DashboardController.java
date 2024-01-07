package Jafari.Mahdi.JimAcademy.controllers;

import Jafari.Mahdi.JimAcademy.carriers.WebToast;
import Jafari.Mahdi.JimAcademy.entities.Course;
import Jafari.Mahdi.JimAcademy.entities.Teacher;
import Jafari.Mahdi.JimAcademy.entities.User;
import Jafari.Mahdi.JimAcademy.repositories.CourseRepository;
import Jafari.Mahdi.JimAcademy.repositories.StudentRepository;
import Jafari.Mahdi.JimAcademy.repositories.TeacherRepository;
import Jafari.Mahdi.JimAcademy.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;


@Controller
@RequestMapping("/dashboard")
@PreAuthorize("@permissionEvaluator.isValidUser()")
public class DashboardController {

    final HttpSession session;
    final UserRepository userRepository;
    final StudentRepository studentRepository;
    final TeacherRepository teacherRepository;
    final CourseRepository courseRepository;

    public DashboardController(HttpSession session, UserRepository userRepository, StudentRepository studentRepository, TeacherRepository teacheRepository, CourseRepository courseRepository) {
        this.session = session;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacheRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public ModelAndView init(@ModelAttribute("map") ModelMap map) {
        List<Course> courseList = courseRepository.findAll();
        return returnUserValidationModel("داشبورد", "dashboard", map);
    }

    @PostMapping("/checkIsClassActive")
    @ResponseBody
    public Map<Integer, Boolean> checkIsClassActive() {
        Map<Integer, Boolean> map = new HashMap<>();
        for (Course course : courseRepository.findAll()) {
            if (ClassRestController.classesStatusMap.get(course.getId()) != null && new Date().getTime() - ClassRestController.classesStatusMap.get(course.getId()).getTime() < 3000L) {
                map.put(course.getId(), true);
            } else {
                map.put(course.getId(), false);
            }
        }
        return map;
    }

    @GetMapping("/management")
    public ModelAndView goToManagementPage(@ModelAttribute("map") ModelMap map, @ModelAttribute Course course) {
        map.put("students", studentRepository.findAll());
        List<Teacher> teacherList = teacherRepository.findAll();
        map.put("teachers", teacherList);
        List<User> userList = new LinkedList<>();
        for (User user : userRepository.findAll()) {
            for (Teacher teacher : teacherList) {
                if (!user.getUsername().equals(teacher.getUsername())) {
                    userList.add(user);
                }
            }
        }
        map.put("users", userList);
        return returnUserValidationModel("مدیریت", "management", map);
    }

    @GetMapping("/makeTeacher")
    public RedirectView makeTeacher(@RequestParam Long userId, RedirectAttributes attributes) {
        ModelMap map = new ModelMap();
        try {
            User user = userRepository.findById(userId).get();
            Teacher teacher = (Teacher) user;
            teacherRepository.save(teacher);
        } catch (Exception e) {
            map.put("toast", new WebToast(WebToast.ToastType.ERROR_TYPE.getValue(), "مشکلی در ارتقای این کاربر به وجود آمده است"));
            attributes.addFlashAttribute("map", map);
            return new RedirectView("management");
        }
        map.put("toast", new WebToast(WebToast.ToastType.CONFIRMATION_TYPE.getValue(), "استاد جدید ثبت شد"));
        attributes.addFlashAttribute("map", map);
        return new RedirectView("management");

    }

    @PostMapping("/newCourse")
    public ModelAndView saveNewCourse(@ModelAttribute Course course, @ModelAttribute ModelMap map) {
        session.setAttribute("course", course);
        map.put("teachers", teacherRepository.findAll());
        return returnUserValidationModel("انتخاب استاد کلاس", "teacherSelectionNewCourse", map);
    }

    @GetMapping("/selectTeacher")
    public RedirectView selectTeacher(@RequestParam Long teacherId, RedirectAttributes attributes, @ModelAttribute ModelMap map) {
        try {
            Course course = (Course) session.getAttribute("course");
            Teacher teacher = teacherRepository.findById(teacherId).get();
            course.setTeacher(teacher);
            courseRepository.save(course);

        } catch (Exception e) {
            map.put("toast", new WebToast(WebToast.ToastType.ERROR_TYPE.getValue(), "مشکلی در ثبت این کلاس به وجود آمده است"));
            attributes.addFlashAttribute("map", map);
            return new RedirectView("management");
        }
        map.put("toast", new WebToast(WebToast.ToastType.CONFIRMATION_TYPE.getValue(), " کلاس ثبت شد"));
        attributes.addFlashAttribute("map", map);
        return new RedirectView("/dashboard");

    }



    public ModelAndView returnUserValidationModel(String title, String viewName, ModelMap map) {
        User sessionUser = ((User) session.getAttribute("currentUser"));
        if (sessionUser != null) for (User tmpUser : userRepository.findAll()) {
            if (sessionUser.getId() == tmpUser.getId() && sessionUser.getUsername().equals(tmpUser.getUsername()) && sessionUser.getPassword().equals(tmpUser.getPassword())) {
                map.put("title", title);
                map.put("user", tmpUser);
                map.put("courses", courseRepository.findAll());
                if (tmpUser.getUsername().equals("admin")) map.put("isAdmin", true);
                else map.put("isAdmin", false);
                return new ModelAndView(viewName, map);
            }
        }
        return new ModelAndView("redirect:/login", map);
    }
}
