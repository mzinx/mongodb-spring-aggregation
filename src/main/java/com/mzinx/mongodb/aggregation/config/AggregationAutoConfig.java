package com.mzinx.mongodb.aggregation.config;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
@Import(AutoConfigurationPackageRegistrar.class)
public class AggregationAutoConfig {

    /**
     * Default POJO codec registry; backs off when the host application
     * defines its own {@link CodecRegistry} bean.
     */
    @Bean
    @ConditionalOnMissingBean(CodecRegistry.class)
    public CodecRegistry pojoCodecRegistry() {
        return CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    }
}
