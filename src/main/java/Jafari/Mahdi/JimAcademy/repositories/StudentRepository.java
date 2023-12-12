package Jafari.Mahdi.JimAcademy.repositories;

import Jafari.Mahdi.JimAcademy.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}