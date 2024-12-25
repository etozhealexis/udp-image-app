package ru.etozhealexis.client2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.messaging.MessageChannel;

/**
 * Configuration of UDP connection
 */
@Configuration
public class UdpConfig {

    @Value("${udp.channel}")
    private String channel;
    @Value("${udp.internal.port}")
    private Integer internalPort;
    @Value("${udp.external.port}")
    private Integer externalPort;

    @Bean
    public MessageChannel inboundChannel() {
        return new DirectChannel();
    }

    @Bean(name = "udpReceivingAdapter")
    public UnicastReceivingChannelAdapter udpReceivingAdapter() {
        UnicastReceivingChannelAdapter adapter = new UnicastReceivingChannelAdapter(internalPort);
        adapter.setOutputChannel(inboundChannel());
        adapter.setOutputChannelName(channel);
        return adapter;
    }

    @Bean
    public UnicastSendingMessageHandler udpSendingAdapter() {
        return new UnicastSendingMessageHandler("localhost", externalPort);
    }

}
