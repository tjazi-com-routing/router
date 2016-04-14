package com.tjazi.routing.router.client;

import com.tjazi.routing.router.client.utils.RouterQueuesNameGenerator;
import com.tjazi.routing.router.messages.interrouter.ClientAddressPerEndpointRecord;
import com.tjazi.routing.router.messages.interrouter.RouterToRouterPayloadMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Krzysztof Wasiak on 30/03/2016.
 */
@Service
public class RouterToRouterClientImpl implements RouterToRouterClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RouterQueuesNameGenerator routerQueuesNameGenerator;

    /**
     * Name of the region which this client is deployed to
     */
    @Value("${region.local.name}")
    private String localRegionName;

    private final static Logger log = LoggerFactory.getLogger(RouterToRouterClientImpl.class);

    public void routeMessage(String targetRouterId,
                             List<ClientAddressPerEndpointRecord> targetAddressesPerEndpoint,
                             String payload) {

        this.assertInputParams(targetRouterId, targetAddressesPerEndpoint, payload);

        String targetQueueName = routerQueuesNameGenerator.generateRouterToRouterQueueName(localRegionName);

        RouterToRouterPayloadMessage message = new RouterToRouterPayloadMessage();
        message.setPayload(payload);
        message.setTargetAddressesPerEndpoint(targetAddressesPerEndpoint);

        rabbitTemplate.convertAndSend(targetQueueName, message);
    }

    private void assertInputParams(String targetRouterId,
                                   List<ClientAddressPerEndpointRecord> targetAddressesPerEndpoint,
                                   String payload) {

        String error = null;

        if (targetRouterId == null || targetRouterId.isEmpty()) {
            error = "targetRouterId is null or empty";
        }

        if (targetAddressesPerEndpoint == null || targetAddressesPerEndpoint.isEmpty()) {
             error = "targetAddressesPerEndpoint is null or empty";
        }

        if (payload == null || payload.isEmpty()) {
            error = "payload is null or empty";
        }

        if (error != null) {
            log.error(error);
            throw new IllegalArgumentException(error);
        }
    }
}
