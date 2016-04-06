package unittests.utils;

import com.tjazi.routing.router.client.utils.RouterQueuesNameGenerator;
import com.tjazi.routing.router.client.utils.RouterQueuesNameGeneratorImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by Krzysztof Wasiak on 05/04/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class RouterQueuesNameGenerator_Test {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @InjectMocks
    public RouterQueuesNameGeneratorImpl routerQueuesNameGenerator;

    @Test
    public void generateRouterToRouterQueueName_nullRegionName_Test() {
        thrown.expect(IllegalArgumentException.class);

        routerQueuesNameGenerator.generateRouterToRouterQueueName(null);
    }

    @Test
    public void generateRouterToRouterQueueName_validRegionName_Test() {

        final String regionName = UUID.randomUUID().toString();

        final String expectedQueueName = String.format("router_%s_r2r_queue", regionName);

        String actualQueueName = routerQueuesNameGenerator.generateRouterToRouterQueueName(regionName);

        assertEquals(expectedQueueName, actualQueueName);
    }

    @Test
    public void generateEndpointToRouterQueueName_nullRegionName_Test() {
        thrown.expect(IllegalArgumentException.class);

        routerQueuesNameGenerator.generateEndpointToRouterQueueName(null);
    }

    @Test
    public void generateEndpointToRouterQueueName_validRegionName_Test() {

        final String regionName = UUID.randomUUID().toString();

        final String expectedQueueName = String.format("router_%s_e2r_queue", regionName);

        String actualQueueName = routerQueuesNameGenerator.generateEndpointToRouterQueueName(regionName);

        assertEquals(expectedQueueName, actualQueueName);
    }
}
