package mx.com.ciat.cio.market.platform.marketalertservice.infrastructure.adapters.in.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.com.ciat.cio.market.platform.marketalertservice.domain.model.Signal;
import mx.com.ciat.cio.market.platform.marketalertservice.domain.port.out.NotificationPort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignalConsumer {

    private final NotificationPort notificationPort;

    @KafkaListener(topics = "market-signals", groupId = "notification-group")
    public void consumeSignal(Signal signal) {
        log.info("🚨 SEÑAL RECIBIDA: {}", signal);
        notificationPort.sendAlert(signal); // 👈 Dispara las notificaciones
    }
}