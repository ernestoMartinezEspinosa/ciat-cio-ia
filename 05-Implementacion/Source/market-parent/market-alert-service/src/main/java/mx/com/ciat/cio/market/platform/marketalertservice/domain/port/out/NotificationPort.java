package mx.com.ciat.cio.market.platform.marketalertservice.domain.port.out;


import mx.com.ciat.cio.market.platform.marketalertservice.domain.model.Signal;


public interface NotificationPort {

    void sendAlert(Signal signal);

}

