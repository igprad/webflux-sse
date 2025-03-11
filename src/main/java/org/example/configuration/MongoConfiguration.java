/* igprad - (C) 2025 */
package org.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@Configuration
@EnableReactiveMongoAuditing
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {

    @Value("${spring.data.mongodb.database:test}")
    private String database;

    @Override
    public String getDatabaseName() {
        return this.database;
    }
}
