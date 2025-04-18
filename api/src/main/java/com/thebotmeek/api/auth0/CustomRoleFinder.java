package com.thebotmeek.api.auth0;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.token.DefaultRolesFinder;
import io.micronaut.security.token.RolesFinder;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton
@Replaces(DefaultRolesFinder.class)
public class CustomRoleFinder implements RolesFinder {

    @Override
    public @NonNull List<String> resolveRoles(
            @Nullable Map<String, Object> attributes) {

        var rolesObj = attributes != null ?
                attributes.get("foodorder_roles") :
                null;

        if (rolesObj == null) {
            return Collections.emptyList();
        }

        List<String> rolesFound = new ArrayList<>();

        // map values from the role attribute
        if (rolesObj instanceof Iterable<?> roles) {
            roles.forEach(role -> rolesFound.add(role.toString()));
        }

        return rolesFound;
    }
}
