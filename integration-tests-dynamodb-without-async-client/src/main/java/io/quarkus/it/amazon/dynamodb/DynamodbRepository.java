package io.quarkus.it.amazon.dynamodb;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.UUID;

@ApplicationScoped
public class DynamodbRepository {
    private final static String BLOCKING_TABLE = "blocking";

    private static final Logger LOG = Logger.getLogger(DynamodbRepository.class);

    @Inject
    DynamoDbClient dynamoClient;

    public String getBlockingEntry() {
        LOG.info("Testing Blocking Dynamodb client with table: " + BLOCKING_TABLE);

        String keyValue = UUID.randomUUID().toString();
        String rangeValue = UUID.randomUUID().toString();
        GetItemResponse item = null;

        if (DynamoDBUtils.createTableIfNotExists(dynamoClient, BLOCKING_TABLE)) {
            if (dynamoClient.putItem(DynamoDBUtils.createPutRequest(BLOCKING_TABLE, keyValue, rangeValue, "OK")) != null) {
                item = dynamoClient.getItem(DynamoDBUtils.createGetRequest(BLOCKING_TABLE, keyValue, rangeValue));
            }
        }

        if (item != null) {
            return item.item().get(DynamoDBUtils.PAYLOAD_NAME).s();
        } else {
            return "ERROR";
        }
    }
}
