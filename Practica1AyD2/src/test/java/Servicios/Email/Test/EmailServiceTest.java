/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Email.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.servicios.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void enviarCorreoCodigo_DeberiaConfigurarYEnviarMensajeCorrectamente() {
        // Arrange
        String destino = "test@ejemplo.com";
        String codigo = "123456";

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Act
        emailService.enviarCorreoCodigo(destino, codigo);

        // Assert
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage mensajeEnviado = messageCaptor.getValue();
        
        assertNotNull(mensajeEnviado.getTo());
        assertEquals(destino, mensajeEnviado.getTo()[0]);
        assertEquals("Código de Recuperación de Contraseña", mensajeEnviado.getSubject());
        assertTrue(mensajeEnviado.getText().contains(codigo));
        assertTrue(mensajeEnviado.getText().contains("15 minutos"));
    }
}