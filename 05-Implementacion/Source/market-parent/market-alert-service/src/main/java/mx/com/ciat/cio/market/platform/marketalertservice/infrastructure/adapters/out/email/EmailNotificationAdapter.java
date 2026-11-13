package mx.com.ciat.cio.market.platform.marketalertservice.infrastructure.adapters.out.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketalertservice.domain.model.Signal;
import mx.com.ciat.cio.market.platform.marketalertservice.domain.port.out.NotificationPort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationAdapter implements NotificationPort {

    private final JavaMailSender mailSender;

    @Override
    public void sendAlert(Signal signal) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // Cambia esto por tu correo real de destino
            message.setTo("ernesto.tcs@gmail.com"); 
            message.setSubject("🚨 Alerta CIAT CIO IA: " + signal.symbol() + " - " + signal.type());
            
            String body = String.format(
                    "Señal: %s\n" +
                    "Símbolo: %s\n" +
                    "Precio: $%s\n" +
                    "Score: %d/100\n" +
                    "Stop Loss: $%s\n" +
                    "Target 1R: $%s\n" +
                    "Target 2R: $%s\n" +
                    "Razón: %s",
                    signal.type(), signal.symbol(), signal.price(), signal.score(),
                    signal.stopLoss(), signal.target1R(), signal.target2R(), signal.reason()
            );
            message.setText(body);
            
            mailSender.send(message);
            log.info("📧 Email enviado exitosamente para {}", signal.symbol());
        } catch (Exception e) {
            log.error("❌ Error enviando email para {}: {}", signal.symbol(), e.getMessage(), e);
        }
    }
}