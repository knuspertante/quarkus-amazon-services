package io.quarkus.amazon.ssm.runtime;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;

import io.quarkus.arc.DefaultBean;
import software.amazon.awssdk.services.ssm.SsmAsyncClient;
import software.amazon.awssdk.services.ssm.SsmAsyncClientBuilder;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.SsmClientBuilder;

@ApplicationScoped
public class SsmClientProducer {
    private final SsmClient syncClient;
    private final SsmAsyncClient asyncClient;

    SsmClientProducer(Instance<SsmClientBuilder> syncClientBuilderInstance,
            Instance<SsmAsyncClientBuilder> asyncClientBuilderInstance) {
        this.syncClient = syncClientBuilderInstance.isResolvable() ? syncClientBuilderInstance.get().build() : null;
        this.asyncClient = asyncClientBuilderInstance.isResolvable() ? asyncClientBuilderInstance.get().build() : null;
    }

    @DefaultBean
    @Produces
    @ApplicationScoped
    public SsmClient client() {
        if (syncClient == null) {
            throw new IllegalStateException("The SsmClient is required but has not been detected/configured.");
        }
        return syncClient;
    }

    @DefaultBean
    @Produces
    @ApplicationScoped
    public SsmAsyncClient asyncClient() {
        if (asyncClient == null) {
            throw new IllegalStateException("The SsmAsyncClient is required but has not been detected/configured.");
        }
        return asyncClient;
    }

    @PreDestroy
    public void destroy() {
        if (syncClient != null) {
            syncClient.close();
        }
        if (asyncClient != null) {
            asyncClient.close();
        }
    }
}
