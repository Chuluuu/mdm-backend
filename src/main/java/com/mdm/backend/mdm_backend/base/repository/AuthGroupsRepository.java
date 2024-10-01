package com.mdm.backend.mdm_backend.base.repository;

import com.mdm.backend.mdm_backend.base.models.AuthGroups;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthGroupsRepository extends JpaRepository<AuthGroups,Long> {
    AuthGroups findByName(String name);
    AuthGroups findByGroupName(String name);
    // Method to delete records from the auth_group_users join table
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM auth_group_users WHERE group_id = :groupId", nativeQuery = true)
    void deleteUsersByGroupId(@Param("groupId") Long groupId);
}
