package com.mdm.backend.mdm_backend.base.repository;

import com.mdm.backend.mdm_backend.base.models.AuthUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.security.core.userdetails.UserDetails;

@Repository
public interface AuthUsersRepository extends JpaRepository<AuthUsers,Long> {

    AuthUsers findByUsername(String username);
    AuthUsers findByEmail(String email);

}
