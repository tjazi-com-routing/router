package com.tjazi.routing.router.client;

import com.tjazi.routing.router.messages.interrouter.ClientAddressPerEndpointRecord;

import java.util.List;

/**
 * Created by Krzysztof Wasiak on 30/03/2016.
 */
public interface RouterToRouterClient {

    void routeMessage(String targetRouterId, List<ClientAddressPerEndpointRecord> targetAddressesPerEndpoint, String payload);
}
