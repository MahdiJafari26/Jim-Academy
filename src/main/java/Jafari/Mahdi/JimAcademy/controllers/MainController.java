package Jafari.Mahdi.JimAcademy.controllers;

import Jafari.Mahdi.JimAcademy.Repositories.StudentRepository;
import Jafari.Mahdi.JimAcademy.carriers.WebToast;
import Jafari.Mahdi.JimAcademy.entities.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class MainController {

    final
    StudentRepository studentRepository;

    public MainController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/login")
    public ModelAndView init(@ModelAttribute ModelMap map) {
        map.put("title" , "ورود");
        return new ModelAndView("login", map);
    }

    @GetMapping("/signup")
    public ModelAndView signupForm(@ModelAttribute("map") ModelMap map,@ModelAttribute Student student){
        map.put("title" , "ثبت نام");
        return new ModelAndView("signup", map);

    }

    @PostMapping("/signup")
    public RedirectView signupSubmit(@ModelAttribute Student student, RedirectAttributes attributes){
        ModelMap map = new ModelMap();
        try {
            studentRepository.save(student);
        }catch (Exception e){
            map.put("toast" , new WebToast(WebToast.ToastType.ERROR_TYPE.getValue(),  "مشکلی در ثبت نام به وجود آمده است"));
            attributes.addFlashAttribute("map" , map);
            return new RedirectView("signup");
        }
        map.put("toast" , new WebToast(WebToast.ToastType.CONFIRMATION_TYPE.getValue(),  "نام شما با موفقیت انجام شد"));
        attributes.addFlashAttribute("map" , map);
        return new RedirectView("login");

    }
}
