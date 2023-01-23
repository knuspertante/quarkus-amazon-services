package io.quarkus.it.amazon.dynamodb.enhanced;

import static software.amazon.awssdk.enhanced.dynamodb.mapper.UpdateBehavior.WRITE_IF_NOT_EXISTS;

import java.time.Instant;

import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbAutoGeneratedTimestampAttribute;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbVersionAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbUpdateBehavior;

@DynamoDbBean
@RegisterForReflection
public class DynamoDBExampleTableEntry {

    private String keyId;

    private String rangeId;

    private String payload;

    private Long version;

    private Instant createdAtTimestamp;

    private Instant updatedAt;

    @DynamoDbPartitionKey
    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getRangeId() {
        return rangeId;
    }

    public void setRangeId(String rangeId) {
        this.rangeId = rangeId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @DynamoDbVersionAttribute
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @DynamoDbAutoGeneratedTimestampAttribute
    @DynamoDbUpdateBehavior(value = WRITE_IF_NOT_EXISTS)
    public Instant getCreatedAtTimestamp() {
        return createdAtTimestamp;
    }

    public void setCreatedAtTimestamp(Instant createdAtTimestamp) {
        this.createdAtTimestamp = createdAtTimestamp;
    }

    @DynamoDbAutoGeneratedTimestampAttribute
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}