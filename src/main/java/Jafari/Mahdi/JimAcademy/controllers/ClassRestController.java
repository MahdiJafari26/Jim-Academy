package Jafari.Mahdi.JimAcademy.controllers;


import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/class")
public class ClassRestController {
    public static Map<Integer, Date> classesStatusMap = new HashMap<>();

    @RequestMapping("/{classNumber}")
    @ResponseBody
    public Boolean setStatus(@PathVariable Integer classNumber) {
        classesStatusMap.put(classNumber , new Date());
        return true;
    }
}
