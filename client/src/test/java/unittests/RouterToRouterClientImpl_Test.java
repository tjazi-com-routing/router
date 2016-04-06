package unittests;

import com.tjazi.routing.router.client.RouterToRouterClientImpl;
import com.tjazi.routing.router.client.utils.RouterQueuesNameGenerator;
import com.tjazi.routing.router.messages.interrouter.ClientAddressPerEndpointRecord;
import com.tjazi.routing.router.messages.interrouter.RouterToRouterPayloadMessage;
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Krzysztof Wasiak on 30/03/2016.
 */

@RunWith(MockitoJUnitRunner.class)
public class RouterToRouterClientImpl_Test {

    private final static String LOCAL_REGION_NAME = UUID.randomUUID().toString();

    @InjectMocks
    public RouterToRouterClientImpl routerToRouterClient;

    @Mock
    public RabbitTemplate rabbitTemplate;

    @Mock
    public RouterQueuesNameGenerator routerQueuesNameGenerator;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        ReflectionTestUtils.setField(routerToRouterClient, "localRegionName", LOCAL_REGION_NAME);
    }

    @Test
    public void sendMessagePayloadToEndpointClients_nullTargetRouterId_Test() {
        thrown.expect(IllegalArgumentException.class);

        routerToRouterClient.routeMessage(null, null, null);
    }

    @Test
    public void sendMessagePayloadToEndpointClients_nulltargetAddressesPerEndpoint_Test() {
        thrown.expect(IllegalArgumentException.class);

        routerToRouterClient.routeMessage("TargetRouter", null, null);
    }

    @Test
    public void sendMessagePayloadToEndpointClients_nullPayload_Test() {
        thrown.expect(IllegalArgumentException.class);

        routerToRouterClient.routeMessage("TargetRouter", Collections.singletonList(new ClientAddressPerEndpointRecord()), null);
    }

    @Test
    public void sendMessageToRouterQueue_Test() {

        final String routerId = "TargetSampleRouter";
        final String expectedQueueName = UUID.randomUUID().toString();
        final List<ClientAddressPerEndpointRecord> sampleEndpoints = Collections.singletonList(new ClientAddressPerEndpointRecord());
        final String samplePayload = "Sample message payload" + UUID.randomUUID();

        when(routerQueuesNameGenerator.generateRouterToRouterQueueName(LOCAL_REGION_NAME))
                .thenReturn(expectedQueueName);

        //
        // execute method
        //
        routerToRouterClient.routeMessage(routerId, sampleEndpoints, samplePayload);

        ArgumentCaptor<RouterToRouterPayloadMessage> payloadMessageCaptor = ArgumentCaptor.forClass(RouterToRouterPayloadMessage.class);

        // verify mocks
        verify(routerQueuesNameGenerator, times(1)).generateRouterToRouterQueueName(LOCAL_REGION_NAME);
        verify(rabbitTemplate, times(1)).convertAndSend(eq(expectedQueueName), payloadMessageCaptor.capture());

        // verify fields values
        RouterToRouterPayloadMessage payloadValue = payloadMessageCaptor.getValue();

        assertEquals(samplePayload, payloadValue.getPayload());
        assertEquals(sampleEndpoints, payloadValue.getTargetAddressesPerEndpoint());
    }

}
