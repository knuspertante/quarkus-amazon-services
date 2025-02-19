package io.quarkus.amazon.common.runtime;

import java.util.List;
import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;

/**
 * AWS SDK specific configurations
 */
@ConfigGroup
public interface SdkBuildTimeConfig {

    /**
     * List of execution interceptors that will have access to read and modify the request and response objects as they are
     * processed by the AWS SDK.
     * <p>
     * The list should consists of class names which implements
     * {@code software.amazon.awssdk.core.interceptor.ExecutionInterceptor} interface.
     *
     * @see software.amazon.awssdk.core.interceptor.ExecutionInterceptor
     */
    Optional<List<String>> interceptors(); // cannot be classes as can be runtime initialized (e.g. XRay interceptor)
}
