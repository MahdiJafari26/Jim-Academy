package Jafari.Mahdi.JimAcademy;

import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JimAcademyApplication {

	public static void main(String[] args) {
		SpringApplication.run(JimAcademyApplication.class, args);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	}

}
