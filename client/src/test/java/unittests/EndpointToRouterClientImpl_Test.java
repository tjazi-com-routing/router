package unittests;

import com.tjazi.routing.router.client.EndpointToRouterClientImpl;
import com.tjazi.routing.router.client.utils.RouterQueuesNameGenerator;
import com.tjazi.routing.router.messages.endpoint.EndpointToRouterPayloadMessage;
import com.tjazi.routing.router.messages.endpoint.EndpointToRouterPayloadMessageType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Krzysztof Wasiak on 04/04/2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class EndpointToRouterClientImpl_Test {

    private final static String LOCAL_REGION_NAME = UUID.randomUUID().toString();

    @InjectMocks
    public EndpointToRouterClientImpl endpointToRouterClient;

    @Mock
    public RabbitTemplate rabbitTemplate;

    @Mock
    public RouterQueuesNameGenerator routerQueuesNameGenerator;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        ReflectionTestUtils.setField(endpointToRouterClient, "localRegionName", LOCAL_REGION_NAME);
    }

    @Test
    public void routeMessage_nullMessageType_Test() {

        thrown.expect(IllegalArgumentException.class);

        endpointToRouterClient.routeMessage(null, null, null);
    }

    @Test
    public void routeMessage_nullTargetId_Test() {

        thrown.expect(IllegalArgumentException.class);

        endpointToRouterClient.routeMessage(EndpointToRouterPayloadMessageType.PER2PER, null, null);
    }

    @Test
    public void routeMessage_nullOrEmptyPayload_Test() {

        thrown.expect(IllegalArgumentException.class);

        endpointToRouterClient.routeMessage(EndpointToRouterPayloadMessageType.PER2PER, UUID.randomUUID(), null);
    }

    @Test
    public void routeMessage_successRoute_Test() {

        final String generatedQueueName = UUID.randomUUID().toString();
        final EndpointToRouterPayloadMessageType endpointToRouterPayloadMessageType = EndpointToRouterPayloadMessageType.MULTICAST;
        final UUID messageTarget = UUID.randomUUID();
        final String messagePayload = "sample message payload" + UUID.randomUUID();

        when(routerQueuesNameGenerator.generateEndpointToRouterQueueName(LOCAL_REGION_NAME))
                .thenReturn(generatedQueueName);

        //
        // main function call
        //
        endpointToRouterClient.routeMessage(endpointToRouterPayloadMessageType, messageTarget, messagePayload);

        // mocks validation
        ArgumentCaptor<EndpointToRouterPayloadMessage> endpointToRouterPayloadMessageArgumentCaptor =
                ArgumentCaptor.forClass(EndpointToRouterPayloadMessage.class);

        verify(routerQueuesNameGenerator, times(1)).generateEndpointToRouterQueueName(eq(LOCAL_REGION_NAME));
        verify(rabbitTemplate, times(1)).convertAndSend(eq(generatedQueueName),
                endpointToRouterPayloadMessageArgumentCaptor.capture());

        // assertion
        EndpointToRouterPayloadMessage capturedMessage = endpointToRouterPayloadMessageArgumentCaptor.getValue();

        assertEquals(messagePayload, capturedMessage.getPayload());
        assertEquals(messageTarget, capturedMessage.getTarget());
        assertEquals(endpointToRouterPayloadMessageType, capturedMessage.getMessageType());
    }
}
