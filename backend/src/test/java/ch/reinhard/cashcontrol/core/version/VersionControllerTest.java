package ch.reinhard.cashcontrol.core.version;

import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;

import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class VersionControllerTest {

    @Test
    void liefertVersionAusBuildInfo() {
        Properties properties = new Properties();
        properties.setProperty("version", "1.2.3");
        var controller = new VersionController(Optional.of(new BuildProperties(properties)));

        assertThat(controller.getVersion().getBody().getVersion()).isEqualTo("1.2.3");
    }

    @Test
    void liefertUnbekanntOhneBuildInfo() {
        var controller = new VersionController(Optional.empty());

        assertThat(controller.getVersion().getBody().getVersion()).isEqualTo(VersionController.UNBEKANNT);
    }
}
