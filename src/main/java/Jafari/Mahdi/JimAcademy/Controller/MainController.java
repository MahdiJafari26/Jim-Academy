package Jafari.Mahdi.JimAcademy.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/hello")
    public String hello(ModelMap model) {
        return "index";
    }
}
