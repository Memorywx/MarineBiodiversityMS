package org.gdou.marine.biodiversity.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class SecurityUser implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Integer role;
    private Integer status;
    private Collection<? extends GrantedAuthority> authorities;

    public SecurityUser(Long id, String username, String password, Integer role, Integer status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + getRoleName(role)));
    }

    private String getRoleName(Integer role) {
        return switch (role) {
            case 0 -> "ADMIN";
            case 1 -> "RESEARCHER";
            case 2 -> "STUDENT";
            default -> "PUBLIC";
        };
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != 2;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == 1;
    }
}
