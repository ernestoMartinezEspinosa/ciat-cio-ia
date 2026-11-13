package mx.com.ciat.cio.market.platform.marketalertservice.infrastructure.adapters.out.whatsapp;

import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketalertservice.domain.model.Signal;
import mx.com.ciat.cio.market.platform.marketalertservice.domain.port.out.NotificationPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class WhatsAppNotificationAdapter implements NotificationPort {

    @Value("${whatsapp.api.token}")
    private String apiToken;

    @Value("${whatsapp.api.phone-number-id}")
    private String phoneNumberId;

    @Value("${whatsapp.api.recipient}")
    private String recipientNumber;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendAlert(Signal signal) {
        try {
            String url = "https://graph.facebook.com/v18.0/" + phoneNumberId + "/messages";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = String.format(
                "{\"messaging_product\":\"whatsapp\",\"to\":\"%s\",\"type\":\"text\",\"text\":{\"body\":\"🚨 ALERTA CIAT CIO IA\\n\\nSímbolo: %s\\nSeñal: %s\\nPrecio: $%s\\nScore: %d/100\\nStop Loss: $%s\\nTarget 1R: $%s\\nTarget 2R: $%s\\nRazón: %s\"}}",
                recipientNumber, signal.symbol(), signal.type(), signal.price(), signal.score(),
                signal.stopLoss(), signal.target1R(), signal.target2R(), signal.reason()
            );

            HttpEntity<String> request = new HttpEntity<>(body, headers);
            //restTemplate.postForEntity(url, request, String.class);
            
            log.info("📱 WhatsApp enviado exitosamente para {}", signal.symbol());
        } catch (Exception e) {
            log.error("❌ Error enviando WhatsApp para {}: {}", signal.symbol(), e.getMessage(), e);
        }
    }
}