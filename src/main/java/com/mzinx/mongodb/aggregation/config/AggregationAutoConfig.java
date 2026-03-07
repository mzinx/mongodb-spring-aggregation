package com.mzinx.mongodb.aggregation.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(AggregationProperties.class)
@ConditionalOnProperty(prefix = "aggregation", name = "enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan("com.mzinx.mongodb.aggregation")
@Import(ScanRegistrar.class)
public class AggregationAutoConfig {

}
