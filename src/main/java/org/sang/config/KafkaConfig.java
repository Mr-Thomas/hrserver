package org.sang.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
@Data
public class KafkaConfig {

    @Value("${kafka.bootstrapServers}")
    private String bootstrapServers;
    @Value("${kafka.groupId}")
    private String groupId;
    @Value("${kafka.maxPollRecords}")
    private String maxPollRecords;
    @Value("${kafka.autoOffsetReset}")
    private String autoOffsetReset;
}
