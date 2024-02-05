package ch.reinhard.cashcontrol;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = CashcontrolApplication.class)
@ContextConfiguration(initializers = PostgreSQLContainerInitializer.class)
class CashcontrolApplicationTests {

	@Test
	void contextLoads() {
	}

}
