package Jafari.Mahdi.JimAcademy.controllers;

import Jafari.Mahdi.JimAcademy.carriers.WebToast;
import Jafari.Mahdi.JimAcademy.entities.Course;
import Jafari.Mahdi.JimAcademy.entities.Teacher;
import Jafari.Mahdi.JimAcademy.entities.User;
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

    @PostMapping("/checkIsClassActive")
    @ResponseBody
    public List<Integer> checkIsClassActive() {
        List<Integer> list= new LinkedList<>();
        for (Integer key : ClassController.classesStatusMap.keySet()){
            if (new Date().getTime() - ClassController.classesStatusMap.get(key).getTime() < 3000L){
                list.add(key);
            }
        }
        return list;
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
            userRepository.findById(userId);
        } catch (Exception e) {
            map.put("toast", new WebToast(WebToast.ToastType.ERROR_TYPE.getValue(), "مشکلی در ارتقای این کاربر به وجود آمده است"));
            attributes.addFlashAttribute("map", map);
            return new RedirectView("management");
        }
        map.put("toast", new WebToast(WebToast.ToastType.CONFIRMATION_TYPE.getValue(), "استاد جدید ثبت شد"));
        attributes.addFlashAttribute("map", map);
        return new RedirectView("management");

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
