/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 *
 * @author eiler
 */
public class GeneratePassword {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

     public  String hashPassword(String password) {
        return encoder.encode(password);
    }

    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
    
    public GeneratePassword(){
        
    }
}
