package com.messenger.configuration;

import com.mongodb.MongoClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

import java.util.Collection;

import static org.junit.Assert.assertNotNull;

public class MongoConfigurationTest {

    private ConfigurableEnvironment env;
    private MongoConfiguration configToTest;

    @Before
    public void setUp() {
        this.env = new StandardEnvironment();
        this.configToTest = new MongoConfiguration(this.env);
    }

    @Test
    public void testGetDatabaseName() {
        TestPropertyValues.of("mongo.database=messengerdb").applyTo(this.env);
        String actual = configToTest.getDatabaseName();
        assertNotNull(actual);
    }

    @Test
    public void testMongoClient() {
        TestPropertyValues.of("mongo.host=127.0.0.1").applyTo(this.env);
        TestPropertyValues.of("mongo.port=27017").applyTo(this.env);
        MongoClient actual = configToTest.mongoClient();
        assertNotNull(actual);
    }

    @Test
    public void testGetMappingBasePackages() {
        Collection<String> actual = configToTest.getMappingBasePackages();
        assertNotNull(actual);
    }
}