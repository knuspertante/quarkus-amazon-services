package io.quarkus.it.amazon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.it.amazon.dynamodb.DynamodbRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AmazonDynamodbTest {

    @Inject
    DynamodbRepository repository;

    @Test
    public void testDynamoDbBlocking() {
        assertEquals("INTERCEPTED OK", repository.getBlockingEntry());
    }
}
