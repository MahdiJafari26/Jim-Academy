package Jafari.Mahdi.JimAcademy.Repository;

import Jafari.Mahdi.JimAcademy.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}