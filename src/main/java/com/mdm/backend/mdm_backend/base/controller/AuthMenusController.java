package com.mdm.backend.mdm_backend.base.controller;

import com.mdm.backend.mdm_backend.base.DTO.groups.CreateGroupDTO;
import com.mdm.backend.mdm_backend.base.DTO.menus.CreateMenuDTO;
import com.mdm.backend.mdm_backend.base.models.AuthGroups;
import com.mdm.backend.mdm_backend.base.models.AuthMenus;
import com.mdm.backend.mdm_backend.base.services.AuthMenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/menus")
public class AuthMenusController {

    private final AuthMenuService authMenuService;

    public AuthMenusController(AuthMenuService authMenuService) {
        this.authMenuService = authMenuService;
    }

    // Get all menus
    @GetMapping
    public ResponseEntity<List<AuthMenus>> getAllMenus() {
        List<AuthMenus> menus = authMenuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }

    // Get a specific menu by ID
    @GetMapping("/{id}")
    public ResponseEntity<AuthMenus> getMenuById(@PathVariable Long id) {
        return authMenuService.getMenuById(id)
                .map(menu -> ResponseEntity.ok(menu))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Create a new menu
    @PostMapping
    public ResponseEntity<AuthMenus> createGroup(@RequestBody CreateMenuDTO createMenuDTO) {
        AuthMenus createdMenu = authMenuService.create(createMenuDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenu);
    }

    // Update an existing menu
    @PutMapping("/{id}")
    public ResponseEntity<AuthMenus> updateGroup(@PathVariable Long id, @RequestBody CreateMenuDTO createMenuDTO) {
        return authMenuService.update(id, createMenuDTO)
                .map(updatedMenu -> ResponseEntity.ok(updatedMenu))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete a menu by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long id) {
        return authMenuService.delete(id)
                .map(user -> ResponseEntity.ok("Menu deleted successfully"))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu not found"));
    }
}
