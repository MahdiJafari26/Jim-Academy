package Jafari.Mahdi.JimAcademy.repositories;

import Jafari.Mahdi.JimAcademy.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}