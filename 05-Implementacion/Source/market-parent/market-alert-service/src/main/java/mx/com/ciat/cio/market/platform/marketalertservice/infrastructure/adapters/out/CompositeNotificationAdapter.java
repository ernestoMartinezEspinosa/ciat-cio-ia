package mx.com.ciat.cio.market.platform.marketalertservice.infrastructure.adapters.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketalertservice.domain.model.Signal;
import mx.com.ciat.cio.market.platform.marketalertservice.domain.port.out.NotificationPort;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Primary //Primario
@Component
@RequiredArgsConstructor
public class CompositeNotificationAdapter implements NotificationPort {

    private final List<NotificationPort> notificationPorts;

    @Override
    public void sendAlert(Signal signal) {
        notificationPorts.forEach(port -> {
            try {
                port.sendAlert(signal);
            } catch (Exception e) {
                log.error("Error en adaptador {}: {}", port.getClass().getSimpleName(), e.getMessage());
            }
        });
    }
}