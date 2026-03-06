package org.mongodb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@ConfigurationProperties("aggregation")
@Component
public class AggregationProperties {
    private boolean enabled = true;
    private String pipelineCollection = "_pipelines";
}
