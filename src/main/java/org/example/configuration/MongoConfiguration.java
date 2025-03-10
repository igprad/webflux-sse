/* igprad - (C) 2025 */
package org.example.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {

    // todo : change this later using proper configuration
    @Override
    protected String getDatabaseName() {
        return "test";
    }
}
