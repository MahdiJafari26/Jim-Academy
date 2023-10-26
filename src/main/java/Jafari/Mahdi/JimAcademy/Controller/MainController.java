package Jafari.Mahdi.JimAcademy.Controller;

import Jafari.Mahdi.JimAcademy.Entity.Course;
import Jafari.Mahdi.JimAcademy.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Random;

@Controller
public class MainController {

    @Autowired
    CourseRepository courseRepository;

    @GetMapping("/hello")
    public String hello(ModelMap model) {
        Course course =new Course();
        course.setId( new Random().nextLong());
        course.setExamList(new ArrayList<>());
        course.setHomeworkList(new ArrayList<>());
        course.setTeacherList(new ArrayList<>());
        course.setInformation("Hello");
        courseRepository.save(course);
        return "index";
    }
}
