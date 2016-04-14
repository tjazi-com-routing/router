package com.tjazi.routing.router.client;

import com.tjazi.routing.router.client.utils.RouterQueuesNameGenerator;
import com.tjazi.routing.router.messages.endpoint.EndpointToRouterPayloadMessage;
import com.tjazi.routing.router.messages.endpoint.EndpointToRouterPayloadMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Krzysztof Wasiak on 30/03/2016.
 */
@Service
public class EndpointToRouterClientImpl implements EndpointToRouterClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RouterQueuesNameGenerator routerQueuesNameGenerator;

    /**
     * Name of the region which this client is deployed to
     */
    @Value("${region.local.name}")
    private String localRegionName;

    private final static Logger log = LoggerFactory.getLogger(EndpointToRouterClientImpl.class);

    public void routeMessage(EndpointToRouterPayloadMessageType messageType, UUID targetId, String payload) {

        this.assertInputParams(messageType, targetId, payload);

        // get the queue name
        String targetQueueName = routerQueuesNameGenerator.generateEndpointToRouterQueueName(localRegionName);

        EndpointToRouterPayloadMessage endpointToRouterPayloadMessage = new EndpointToRouterPayloadMessage();
        endpointToRouterPayloadMessage.setPayload(payload);
        endpointToRouterPayloadMessage.setMessageType(messageType);
        endpointToRouterPayloadMessage.setTarget(targetId);

        rabbitTemplate.convertAndSend(targetQueueName, endpointToRouterPayloadMessage);
    }

    private void assertInputParams(EndpointToRouterPayloadMessageType messageType, UUID targetId, String payload) {

        String error = null;

        if (messageType == null) {
            error = "EndpointToRouterClientImpl is null";
        }

        if (targetId == null) {
            error = "targetId is null";
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
