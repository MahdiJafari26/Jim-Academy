package Jafari.Mahdi.JimAcademy.Repositories;

import Jafari.Mahdi.JimAcademy.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}