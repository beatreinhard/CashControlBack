package ch.reinhard.cashcontrol;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;


@Slf4j
@SpringBootApplication
public class CashcontrolApplication {

	public static void main(String[] args) {
		Environment env = SpringApplication.run(CashcontrolApplication.class, args).getEnvironment();
		String appName = env.getProperty("spring.application.name");
		String contextPath = env.getProperty("server.servlet.context-path");
		String serverPort = env.getProperty("server.port");
		String[] profiles = env.getActiveProfiles();

		log.info("");
		log.info("------------------------------------------------");
		log.info("Umgebung: " + StringUtils.join(profiles));
		log.info("App-Name: " + appName);
		log.info("http://localhost:" + serverPort + contextPath + "/api/v1/ausgabe");
		log.info("http://localhost:" + serverPort + contextPath + "/swagger-ui.html");
		log.info("------------------------------------------------");
	}

}
