package Jafari.Mahdi.JimAcademy.controllers;

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
    @GetMapping
    public ModelAndView init(@ModelAttribute("map") ModelMap map) {
        map.put("title", "داشبورد");
        return new ModelAndView("dashboard", map);
    }

}
