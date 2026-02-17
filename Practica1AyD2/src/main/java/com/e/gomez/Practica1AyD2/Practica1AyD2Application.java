package com.e.gomez.Practica1AyD2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import utilities.GeneratePassword;

@SpringBootApplication
public class Practica1AyD2Application {

	public static void main(String[] args) {
		          GeneratePassword gp = new GeneratePassword();
                          System.out.println(gp.hashPassword("admin"));
	}

}
