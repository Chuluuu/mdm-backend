package com.mdm.backend.mdm_backend.base.controller;
import com.mdm.backend.mdm_backend.base.DTO.groups.CreateGroupDTO;
import com.mdm.backend.mdm_backend.base.DTO.models.CreateAuthModelDTO;
import com.mdm.backend.mdm_backend.base.DTO.models.UpdateAuthModelDTO;
import com.mdm.backend.mdm_backend.base.models.AuthGroups;
import com.mdm.backend.mdm_backend.base.models.AuthModels;
import com.mdm.backend.mdm_backend.base.services.AuthModelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/models")
public class AuthModelsController {
    private final AuthModelService authModelService;

    public AuthModelsController(AuthModelService authModelService) {
        this.authModelService = authModelService;
    }

//    @PostMapping("/import")
//    public List<AuthModels> importFromXml(@RequestParam("file") MultipartFile file) throws JAXBException, IOException {
//        Path tempFile = Files.createTempFile(null, ".xml");
//        file.transferTo(tempFile.toFile());
//        return authModelService.importFromXml(tempFile.toFile());
//    }

    // Get all models
    @GetMapping
    public ResponseEntity<List<AuthModels>> getAllGroups() {
        List<AuthModels> models = authModelService.getAllModels();
        return ResponseEntity.ok(models);
    }

    // Get a specific group by ID
    @GetMapping("/{id}")
    public ResponseEntity<AuthModels> getModelById(@PathVariable Long id) {
        return authModelService.getModelById(id)
                .map(group -> ResponseEntity.ok(group))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Create a new group
    @PostMapping
    public ResponseEntity<AuthModels> createModel(@RequestBody CreateAuthModelDTO createAuthModelDTO) {
        AuthModels createdModel = authModelService.create(createAuthModelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdModel);
    }

    // Update an existing group
    @PutMapping("/{id}")
    public ResponseEntity<AuthModels> updateModel(@PathVariable Long id, @RequestBody UpdateAuthModelDTO updateAuthModelDTO) {
        return authModelService.update(id, updateAuthModelDTO)
                .map(updatedModel -> ResponseEntity.ok(updatedModel))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete a group by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteModel(@PathVariable Long id) {
        return authModelService.delete(id)
                .map(user -> ResponseEntity.ok("Model deleted successfully"))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Model not found"));
    }


}
