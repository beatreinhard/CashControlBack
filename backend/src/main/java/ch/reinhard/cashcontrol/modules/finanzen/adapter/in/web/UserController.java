package ch.reinhard.cashcontrol.modules.finanzen.adapter.in.web;

import ch.reinhard.cashcontrol.openapi.api.UserApi;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class UserController implements UserApi {
    @Override
    public ResponseEntity<List<String>> usersGet() {
        return null;
    }
}
