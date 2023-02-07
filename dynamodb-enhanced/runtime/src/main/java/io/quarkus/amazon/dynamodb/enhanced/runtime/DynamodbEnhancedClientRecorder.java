package io.quarkus.amazon.dynamodb.enhanced.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.arc.runtime.BeanContainerListener;
import io.quarkus.runtime.annotations.Recorder;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClientExtension;
import software.amazon.awssdk.enhanced.dynamodb.internal.client.ExtensionResolver;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Recorder
public class DynamodbEnhancedClientRecorder {
    private static final Log LOG = LogFactory.getLog(DynamodbEnhancedClientRecorder.class);

    DynamoDbEnhancedBuildTimeConfig buildTimeConfig;

    public DynamodbEnhancedClientRecorder(DynamoDbEnhancedBuildTimeConfig buildTimeConfig) {
        this.buildTimeConfig = buildTimeConfig;
    }

    public BeanContainerListener setDynamoDbClient() {
        BeanContainerListener beanContainerListener = new BeanContainerListener() {

            @Override
            public void created(BeanContainer container) {
                DynamodbEnhancedClientProducer producer = container.beanInstance(DynamodbEnhancedClientProducer.class);
                producer.setDynamoDbClient(container.beanInstance(DynamoDbClient.class),
                        createExtensionList());
            }
        };

        return beanContainerListener;
    }

    public BeanContainerListener setDynamoDbAsyncClient() {
        BeanContainerListener beanContainerListener = new BeanContainerListener() {

            @Override
            public void created(BeanContainer container) {
                DynamodbEnhancedClientProducer producer = container.beanInstance(DynamodbEnhancedClientProducer.class);
                producer.setDynamoDbAsyncClient(container.beanInstance(DynamoDbAsyncClient.class),
                        createExtensionList());
            }
        };

        return beanContainerListener;
    }

    private List<DynamoDbEnhancedClientExtension> createExtensionList() {

        List<DynamoDbEnhancedClientExtension> extensions = new ArrayList<>();
        buildTimeConfig.clientExtensions.orElse(Collections.emptyList()).stream()
                .map(String::trim)
                .map(this::createExtension)
                .filter(Objects::nonNull)
                .forEach(extensions::add);
        if (extensions.isEmpty()) {
            extensions.addAll(ExtensionResolver.defaultExtensions());
        }

        return extensions;
    }

    private DynamoDbEnhancedClientExtension createExtension(String extensionClassName) {
        try {
            Class<?> clazz = Class
                    .forName(extensionClassName, false, Thread.currentThread().getContextClassLoader());

            Method builderMethod = null;
            try {
                builderMethod = clazz.getMethod("builder");
            } catch (NoSuchMethodException discard) {
            }

            if (builderMethod != null) {
                // try builder pattern in aws sdk
                Object builder = builderMethod.invoke(null);
                DynamoDbEnhancedClientExtension extension = (DynamoDbEnhancedClientExtension) builder.getClass()
                        .getMethod("build").invoke(builder);
                return extension;
            } else {
                return (DynamoDbEnhancedClientExtension) clazz.getDeclaredConstructor().newInstance();
            }
        } catch (ClassNotFoundException | ClassCastException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            LOG.error("Unable to create extension " + extensionClassName, e);
            return null;
        }
    }
}
