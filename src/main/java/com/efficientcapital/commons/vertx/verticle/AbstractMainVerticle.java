package com.efficientcapital.commons.vertx.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.kubernetes.KubernetesServiceImporter;

/**
 * Created by Luminara Team.
 */
public abstract class AbstractMainVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMainVerticle.class);
    protected ServiceDiscovery discovery;
    protected ConfigRetriever configRetriever;

    @Override
    public void start(Promise<Void> startFuture) throws Exception {
        LOGGER.debug("AbstractMainVerticle.start(..)");
        configRetriever();
        configRetriever.getConfig(config -> {
            if (config.failed()) {
                startFuture.fail(config.cause());
            } else {
                discovery = ServiceDiscovery.create(vertx);

                if (config.result().containsKey("KUBERNETES_NAMESPACE")) {
                    discovery.registerServiceImporter(new KubernetesServiceImporter(), new JsonObject());
                } else {
                    System.setProperty("vertx-service-discovery-backend-local", "true");
                }

                deployVerticles(config.result());
            }
        });
    }

    /**
     * Inheriting classes will be required to provide and implementation of
     * this function where business verticles will be deployed
     */
    protected abstract void deployVerticles(JsonObject config);

    private void configRetriever() {
        ConfigStoreOptions environmentStore = new ConfigStoreOptions()
                .setType("env");

        ConfigStoreOptions yamlStore = new ConfigStoreOptions()
                .setType("file")
                .setFormat("yaml")
                .setConfig(new JsonObject()
                        .put("path", "application.yml"));

        configRetriever = ConfigRetriever.create(vertx,
                new ConfigRetrieverOptions()
                        .addStore(environmentStore)
                        .addStore(yamlStore));
    }
}
