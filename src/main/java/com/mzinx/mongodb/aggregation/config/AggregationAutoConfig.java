package com.mzinx.mongodb.aggregation.config;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.mongodb.MongoClientSettings;

@AutoConfiguration
@EnableConfigurationProperties(AggregationProperties.class)
@ConditionalOnProperty(prefix = "aggregation", name = "enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan("com.mzinx.mongodb.aggregation")
@Import(ScanRegistrar.class)
public class AggregationAutoConfig {

    @Bean
    public CodecRegistry pojoCodecRegistry() {
        return CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    }
}
