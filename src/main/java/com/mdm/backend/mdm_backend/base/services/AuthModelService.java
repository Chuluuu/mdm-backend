package com.mdm.backend.mdm_backend.base.services;

import com.mdm.backend.mdm_backend.base.DTO.groups.CreateGroupDTO;
import com.mdm.backend.mdm_backend.base.DTO.models.AuthModelAccessDTO;
import com.mdm.backend.mdm_backend.base.DTO.models.AuthModelDTO;
import com.mdm.backend.mdm_backend.base.DTO.models.CreateAuthModelDTO;
import com.mdm.backend.mdm_backend.base.DTO.models.UpdateAuthModelDTO;
import com.mdm.backend.mdm_backend.base.models.AuthGroups;
import com.mdm.backend.mdm_backend.base.models.AuthModels;
import com.mdm.backend.mdm_backend.base.models.AuthModelsAccess;
import com.mdm.backend.mdm_backend.base.models.AuthUsers;
import com.mdm.backend.mdm_backend.base.repository.AuthGroupsRepository;
import com.mdm.backend.mdm_backend.base.repository.AuthModelsRepository;
import com.mdm.backend.mdm_backend.base.security.AuthModelList;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@Order(3)
public class AuthModelService {

    private final AuthModelsRepository authModelsRepository;
    private final AuthGroupsRepository authGroupsRepository;

    public AuthModelService(AuthModelsRepository authModelsRepository, AuthGroupsRepository authGroupsRepository) {
        this.authModelsRepository = authModelsRepository;
        this.authGroupsRepository = authGroupsRepository;
    }

    public List<AuthModels> importFromXml(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(AuthModelList.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        AuthModelList authModelList = (AuthModelList) unmarshaller.unmarshal(file);

        for (AuthModelDTO authModelDTO : authModelList.getAuthModels()) {
            saveOrUpdateModel(authModelDTO);
        }
        return authModelsRepository.findAll();
    }
    @PostConstruct
    public void loadPermissionsOnStartup() {
        try {
            Path xmlPath = Paths.get("src/main/java/com/mdm/backend/mdm_backend/base/security/permissions.xml");
            if (Files.exists(xmlPath)) {
                importFromXml(xmlPath.toFile());
            }

        } catch (JAXBException e) {
            e.printStackTrace(); // Handle exceptions here (e.g., logging)
        }
    }
    private void saveOrUpdateModel(AuthModelDTO dto) {
        AuthModels existingModel = authModelsRepository.findByModelName(dto.getModelName());
        if (existingModel == null) {
            AuthModels authModel = new AuthModels();
            authModel.setName(dto.getName());
            authModel.setDescription(dto.getDescription());
            authModel.setModelName(dto.getModelName());
            authModel.setActive(dto.getActive());
            List<AuthModelsAccess> accessList = new ArrayList<>();
            for (AuthModelAccessDTO accessDTO : dto.getAccess()) {
                AuthModelsAccess access = new AuthModelsAccess();
                access.setAuthModelId(authModel);
                access.setName(accessDTO.getName());
                access.setGroupId(authGroupsRepository.findByName(accessDTO.getGroupId()));
                access.setPermRead(accessDTO.getPermRead());
                access.setPermCreate(accessDTO.getPermCreate());
                access.setPermUpdate(accessDTO.getPermUpdate());
                access.setPermDelete(accessDTO.getPermDelete());
                accessList.add(access);
            }
            authModel.setAccessIds(accessList);
            authModelsRepository.save(authModel);
        }
    }
    // Get all models
    public List<AuthModels> getAllModels() {
        return authModelsRepository.findAll();
    }
    @Transactional
    public AuthModels create(CreateAuthModelDTO createAuthModelDTO) {

        AuthModels newModel = new AuthModels();
        newModel.setName(createAuthModelDTO.getName());
        newModel.setDescription(createAuthModelDTO.getDescription());

        if (createAuthModelDTO.getAccess() != null) {
            for (Long userId : createAuthModelDTO.getAccess()) {
                authModelsRepository.findById(userId).ifPresent(user -> {
                    AuthModelsAccess access = new AuthModelsAccess();
                    access.setAuthModelId(newModel); // Set the model for the access
//                    access.setGroupId(user); // Assuming user is an AuthGroups instance or update according to your logic
                    // Set permissions as needed, e.g.:
//                    access.setPermRead(true); // Example permission assignment
                    newModel.getAccessIds().add(access); // Add access to the model's accessIds
                });
            }
        }
//        newModel.setAccessIds(access);

        // Save the group and generate ID only on success
        return authModelsRepository.save(newModel);
    }
    // Get group by ID
    public Optional<AuthModels> getModelById(Long id) {
        return authModelsRepository.findById(id);
    }
    @Transactional
    public Optional<AuthModels> update(Long id, UpdateAuthModelDTO updateAuthModelDTO) {
        return authModelsRepository.findById(id).map(model -> {
            model.setName(updateAuthModelDTO.getName());
            model.setDescription(updateAuthModelDTO.getDescription());
            return authModelsRepository.save(model);
        });
    }
    public Optional<AuthModels> delete(Long id) {
        Optional<AuthModels> optionalModel = authModelsRepository.findById(id);
        if (optionalModel.isPresent()) {
            AuthModels authModel = optionalModel.get();
//            #TODO add notDeleteAble feature if its necessary
//            if (authModel.getNotDeleteAble()) {
//                throw new IllegalStateException("This menu cannot be deleted");
//            }
            authModelsRepository.delete(authModel);
            return Optional.of(authModel);
        } else {
            return Optional.empty();
        }
    }
}

