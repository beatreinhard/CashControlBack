package ch.reinhard.cashcontrol.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"ch.reinhard.cashcontrol.modules"})
public class PersistenceConfig {
}
