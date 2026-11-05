package ch.reinhard.cashcontrol.core.version;

import ch.reinhard.cashcontrol.openapi.api.VersionControllerApi;
import ch.reinhard.cashcontrol.openapi.model.VersionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class VersionController implements VersionControllerApi {
    static final String UNBEKANNT = "unbekannt";

    // Fehlt beim Start aus der IDE ohne Maven-Build, die App soll dann trotzdem starten.
    private final Optional<BuildProperties> buildProperties;

    @Override
    public ResponseEntity<VersionDto> getVersion() {
        String version = buildProperties.map(BuildProperties::getVersion).orElse(UNBEKANNT);
        return ResponseEntity.ok(new VersionDto().version(version));
    }
}
