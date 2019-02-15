package com.messenger.configuration;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Configuration
@EnableMongoRepositories(basePackages = "com.messenger")
@PropertySource("classpath:mongo.properties")
public class MongoConfiguration extends AbstractMongoConfiguration {

    private final Environment env;

    public MongoConfiguration(final Environment env) {
        this.env = env;
    }

    @Override
    protected String getDatabaseName() {
        return env.getProperty("mongo.database");
    }

    @Override
    public MongoClient mongoClient() {
        String host = env.getProperty("mongo.host");
        String port = Optional.ofNullable(env.getProperty("mongo.port")).orElse("27017");
        return new MongoClient(host, Integer.parseInt(port));
    }

    @Override
    protected Collection<String> getMappingBasePackages() {
        return Collections.singletonList("messenger");
    }

}
