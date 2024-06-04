package io.arrogantprogrammer;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.quarkus.logging.Log;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class DaprUtil {

    private DaprClient daprClient;

    @ConfigProperty(name = "dapr.pubsub")
    private String PUBSUB_NAME;
    @ConfigProperty(name = "dapr.kvstore")
    private String KVSTORE_NAME;
    @ConfigProperty(name = "dapr.http-endpoint")
    private String DAPR_HTTP_ENDPOINT;
    @ConfigProperty(name = "dapr.api-token.inbound")
    private String DAPR_API_TOKEN;
    @ConfigProperty(name = "dapr.target-appid")
    private String INVOKE_TARGET_APPID;
    @PostConstruct
    public void init() {
        daprClient = new DaprClientBuilder().build();
        Log.debug("DaprClient initialized");
        Log.debugf("PUBSUB_NAME: %s", PUBSUB_NAME);
        Log.debugf("KVSTORE_NAME: %s", KVSTORE_NAME);
        Log.debugf("DAPR_HTTP_ENDPOINT: %s", DAPR_HTTP_ENDPOINT);
        Log.debugf("DAPR_API_TOKEN: %s", DAPR_API_TOKEN);
        Log.debugf("INVOKE_TARGET_APPID: %s", INVOKE_TARGET_APPID);
    }

    void publishEvent(String helloString, String string) {
        daprClient.publishEvent(PUBSUB_NAME, helloString, string).block();
        Log.debugf("Published event to %s: %s", PUBSUB_NAME, helloString);
    }
}
