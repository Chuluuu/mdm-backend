package com.mdm.backend.mdm_backend.base.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mdm.backend.mdm_backend.base.config.Many2OneFieldSerializer;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.*;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchConnectionDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/*
 * Author: Chuluunbor
 * Email: loopersoftdev@gmail.com
 * Company: Looper Soft LLC
 * Created: September 20, 2024
 * Version: 1.0
 * License: MIT License
 * Description: This class handles menu authentication in the application
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class AuthUsers extends BaseEntity implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,name="username")
    private String username;

    @JsonIgnore
    @Column(name="password",unique = true)
    private String password;

    @Column(name="email",unique = true,nullable = false)
    private String email;

    @Column(name="active")
    private Boolean active=true;

    @Column(name="login_date")
    private LocalDateTime loginDate;

    @ManyToOne
    @JoinColumn(name = "create_uid", nullable = true, updatable = false)
    @JsonSerialize(using = Many2OneFieldSerializer.class)
    private AuthUsers createUid;

    @ManyToOne
    @JoinColumn(name = "write_uid", nullable = true)
    @JsonSerialize(using = Many2OneFieldSerializer.class)
    private AuthUsers writeUid;
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
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
        return active;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }


}
