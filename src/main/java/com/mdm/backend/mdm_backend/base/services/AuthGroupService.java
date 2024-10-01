package com.mdm.backend.mdm_backend.base.services;


import com.mdm.backend.mdm_backend.base.DTO.groups.AuthGroupUsersXmlDTO;
import com.mdm.backend.mdm_backend.base.DTO.groups.AuthGroupXmlDTO;
import com.mdm.backend.mdm_backend.base.DTO.groups.CreateGroupDTO;
import com.mdm.backend.mdm_backend.base.models.AuthGroups;
import com.mdm.backend.mdm_backend.base.models.AuthUsers;
import com.mdm.backend.mdm_backend.base.repository.AuthGroupsRepository;
import com.mdm.backend.mdm_backend.base.repository.AuthUsersRepository;
import com.mdm.backend.mdm_backend.base.security.AuthGroupList;
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
@Order(2)
public class AuthGroupService {

    private final AuthGroupsRepository authGroupsRepository;
    private final AuthUsersRepository authUsersRepository;

    public AuthGroupService(AuthGroupsRepository authGroupsRepository, AuthUsersRepository authUsersRepository) {
        this.authGroupsRepository = authGroupsRepository;
        this.authUsersRepository = authUsersRepository;
    }

    @Transactional
    public AuthGroups create(CreateGroupDTO createGroupDTO) {

        AuthGroups newGroup = new AuthGroups();
        newGroup.setName(createGroupDTO.getName());
        newGroup.setDescription(createGroupDTO.getDescription());
        // Associate users with the group
        Set<AuthUsers> users = new HashSet<>();
        if (createGroupDTO.getUserIds() != null) {
            for (Long userId : createGroupDTO.getUserIds()) {
                authUsersRepository.findById(userId).ifPresent(users::add);
            }
        }
        newGroup.setAuthUsers(users);

        // Save the group and generate ID only on success
        return authGroupsRepository.save(newGroup);
    }
    // Get all groups
    public List<AuthGroups> getAllGroups() {
        return authGroupsRepository.findAll();
    }

    // Get group by ID
    public Optional<AuthGroups> getGroupById(Long id) {
        return authGroupsRepository.findById(id);
    }

    // Update group by ID
    @Transactional
    public Optional<AuthGroups> update(Long id, CreateGroupDTO updateGroupDTO) {
        return authGroupsRepository.findById(id).map(group -> {
            group.setName(updateGroupDTO.getName());
            group.setDescription(updateGroupDTO.getDescription());
            return authGroupsRepository.save(group);
        });
    }
    //delete group by ID
    public Optional<AuthGroups> delete(Long id) {
        Optional<AuthGroups> optionalGroup = authGroupsRepository.findById(id);
        if (optionalGroup.isPresent()) {
            AuthGroups group = optionalGroup.get();
            if (group.getNotDeleteAble()) {
                throw new IllegalStateException("This group cannot be deleted");
            }
            authGroupsRepository.deleteUsersByGroupId(id);
            authGroupsRepository.delete(group);
            return Optional.of(group);
        } else {
            return Optional.empty();
        }
    }

    public List<AuthGroups> importFromXml(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(AuthGroupList.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        AuthGroupList authGroupList = (AuthGroupList) unmarshaller.unmarshal(file);

        for (AuthGroupXmlDTO authGroupXmlDTODTO : authGroupList.getAuthGroups()) {
            saveOrUpdateGroup(authGroupXmlDTODTO);
        }
        return authGroupsRepository.findAll();
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
    private void saveOrUpdateGroup(AuthGroupXmlDTO dto) {
        AuthGroups existingGroup = authGroupsRepository.findByGroupName(dto.getGroupName());
        if (existingGroup == null) {
            AuthGroups authGroup = new AuthGroups();
            authGroup.setName(dto.getName());
            authGroup.setDescription(dto.getDescription());
            authGroup.setActive(dto.getActive());
            authGroup.setGroupName(dto.getGroupName());
            authGroup.setNotDeleteAble(dto.getNotDeleteAble());
            Set<AuthUsers> userList = new HashSet<>(); // Use Set to avoid duplicates

            for (AuthGroupUsersXmlDTO userDTO : dto.getUsers()) {
                // Find the existing user by name or other unique identifier
                Optional<AuthUsers> optionalUser = authUsersRepository.findById(userDTO.getUserId());
                if (optionalUser.isPresent()) {
                    AuthUsers user = optionalUser.get(); // Get the user if present
                    userList.add(user); // Add existing user to the set
                }
            }
            authGroup.setAuthUsers(userList);
            authGroupsRepository.save(authGroup);
        }
    }
}
