package com.mdm.backend.mdm_backend.base.repository;

import com.mdm.backend.mdm_backend.base.models.AuthMenus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthMenusRepository extends JpaRepository<AuthMenus,Long> {
    AuthMenus findByName(String name);
    AuthMenus findByMenuName(String name);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM auth_menu_groups WHERE menu_id = :menuId", nativeQuery = true)
    void deleteGroupsByMenuId(@Param("menuId") Long menuId);
}
