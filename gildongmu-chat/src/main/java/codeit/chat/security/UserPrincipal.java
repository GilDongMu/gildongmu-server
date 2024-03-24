package codeit.chat.security;

import codeit.domain.user.entity.User;
import lombok.Getter;

import java.security.Principal;
import java.util.UUID;

public class UserPrincipal implements Principal {
    @Getter
    private User user;
    private final String uuid;

    public UserPrincipal() {
        uuid = UUID.randomUUID().toString();
    }

    @Override
    public String getName() {
        return uuid;
    }

    protected void loadUser(User user) {
        this.user = user;
    }
}
