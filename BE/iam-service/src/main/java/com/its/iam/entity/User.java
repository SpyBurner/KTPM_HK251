package com.its.iam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;
    
    private String phone;

    private String passwordHash;

    private String displayName;
    
    private Boolean active;
    
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserHasPrivilege> userPrivileges;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    private File file;

    
    @Override
    public String getPassword(){
        return passwordHash;
    }

    @Override
    public String getUsername(){
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // 1. Role
        SimpleGrantedAuthority roleAuthority =
                new SimpleGrantedAuthority("ROLE_" + role.getName());

        // 2. Privileges safely
        List<SimpleGrantedAuthority> privilegeAuthorities =
                (userPrivileges == null)
                        ? List.of()
                        : userPrivileges.stream()
                        .map(up -> {
                            var p = up.getPrivilege();
                            return new SimpleGrantedAuthority(p.getName() + ":" + p.getType());
                        })
                        .toList();

        // 3. Combine safely
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(roleAuthority);
        authorities.addAll(privilegeAuthorities);

        return authorities;
    }

}
