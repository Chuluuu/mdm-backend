package com.mdm.backend.mdm_backend.base.controller;

import com.mdm.backend.mdm_backend.base.DTO.groups.CreateGroupDTO;
import com.mdm.backend.mdm_backend.base.models.AuthGroups;
import com.mdm.backend.mdm_backend.base.services.AuthGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/groups")
public class AuthGroupsController {

    private final AuthGroupService  authGroupService;

    public AuthGroupsController(AuthGroupService authGroupService) {
        this.authGroupService = authGroupService;
    }

    // Get all groups
    @GetMapping
    public ResponseEntity<List<AuthGroups>> getAllGroups() {
        List<AuthGroups> groups = authGroupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    // Get a specific group by ID
    @GetMapping("/{id}")
    public ResponseEntity<AuthGroups> getGroupById(@PathVariable Long id) {
        return authGroupService.getGroupById(id)
                .map(group -> ResponseEntity.ok(group))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Create a new group
    @PostMapping
    public ResponseEntity<AuthGroups> createGroup(@RequestBody CreateGroupDTO createGroupDTO) {
        AuthGroups createdGroup = authGroupService.create(createGroupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    // Update an existing group
    @PutMapping("/{id}")
    public ResponseEntity<AuthGroups> updateGroup(@PathVariable Long id, @RequestBody CreateGroupDTO updateGroupDTO) {
        return authGroupService.update(id, updateGroupDTO)
                .map(updatedGroup -> ResponseEntity.ok(updatedGroup))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete a group by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long id) {
        return authGroupService.delete(id)
                .map(user -> ResponseEntity.ok("Group deleted successfully"))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found"));
    }
}
