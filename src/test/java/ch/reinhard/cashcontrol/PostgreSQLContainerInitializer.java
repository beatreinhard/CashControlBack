package ch.reinhard.cashcontrol;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static PostgreSQLContainer<?> database;

    private static PostgreSQLContainer<?> getDatabase() {
        if (database == null) {
            database = new PostgreSQLContainer<>("postgres:latest");
            database.start();
        }
        return database;
    }

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                configurableApplicationContext,
                "spring.datasource.url=" + getDatabase().getJdbcUrl(),
                "spring.datasource.username=" + getDatabase().getUsername(),
                "spring.datasource.password=" + getDatabase().getPassword()
        );
    }
}
