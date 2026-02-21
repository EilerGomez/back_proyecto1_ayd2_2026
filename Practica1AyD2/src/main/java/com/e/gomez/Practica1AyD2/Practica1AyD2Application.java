package com.e.gomez.Practica1AyD2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.e.gomez.Practica1AyD2.utilities.GeneratePassword;

@SpringBootApplication
public class Practica1AyD2Application {

	public static void main(String[] args) {
            GeneratePassword g = new GeneratePassword();
		          SpringApplication.run(Practica1AyD2Application.class, args);
                          System.out.println("Password user admin "+g.hashPassword("admin123"));
	}

}
