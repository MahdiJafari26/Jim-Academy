package Jafari.Mahdi.JimAcademy.repositories;

import Jafari.Mahdi.JimAcademy.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}