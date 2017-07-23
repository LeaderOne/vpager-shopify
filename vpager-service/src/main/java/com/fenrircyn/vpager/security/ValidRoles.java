package com.fenrircyn.vpager.security;

import org.springframework.security.core.GrantedAuthority;

public enum ValidRoles implements GrantedAuthority {
    USER_ROLE(), ADMIN_ROLE(), WEBHOOK_ROLE(), PROXY_ROLE();

    ValidRoles() {
    }


    @Override
    public String getAuthority() {
        return name();
    }
}
