package com.mdm.backend.mdm_backend.base.services;

import com.mdm.backend.mdm_backend.base.DTO.menus.AuthMenuGroupXmlDTO;
import com.mdm.backend.mdm_backend.base.DTO.menus.AuthMenuXmlDTO;
import com.mdm.backend.mdm_backend.base.DTO.menus.CreateMenuDTO;
import com.mdm.backend.mdm_backend.base.models.AuthGroups;
import com.mdm.backend.mdm_backend.base.models.AuthMenus;
import com.mdm.backend.mdm_backend.base.repository.AuthGroupsRepository;
import com.mdm.backend.mdm_backend.base.repository.AuthMenusRepository;
import com.mdm.backend.mdm_backend.base.security.AuthMenuList;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Order(4)
public class AuthMenuService {
    private final  AuthMenusRepository authMenusRepository;
    private final AuthGroupsRepository authGroupsRepository;

    public AuthMenuService(AuthMenusRepository authMenusRepository, AuthGroupsRepository authGroupsRepository) {
        this.authMenusRepository = authMenusRepository;
        this.authGroupsRepository = authGroupsRepository;
    }
    //Create method
    @Transactional
    public AuthMenus create(CreateMenuDTO createMenuDTO) {

        AuthMenus newMenu = new AuthMenus();
        newMenu.setName(createMenuDTO.getName());
        newMenu.setDescription(createMenuDTO.getDescription());
        for (AuthMenus child : newMenu.getChildIds()) {
            if (child.getId() != null && child.getId().equals(newMenu.getId())) {
                throw new IllegalArgumentException("Menu cannot be its own child.");
            }
        }
        //#TODO add groups if its Included
        return authMenusRepository.save(newMenu);
    }

    // Get all groups
    public List<AuthMenus> getAllMenus() {
        return authMenusRepository.findAll();
    }

    // Get group by ID
    public Optional<AuthMenus> getMenuById(Long id) {
        return authMenusRepository.findById(id);
    }
    //Update method
    @Transactional
    public Optional<AuthMenus> update(Long id, CreateMenuDTO createMenuDTO) {
        return authMenusRepository.findById(id).map(menu -> {
            menu.setName(createMenuDTO.getName());
            menu.setDescription(createMenuDTO.getDescription());
            for (AuthMenus child : menu.getChildIds()) {
                if (child.getId() != null && child.getId().equals(menu.getId())) {
                    throw new IllegalArgumentException("Menu cannot be its own child.");
                }
            }
            return authMenusRepository.save(menu);
        });
    }
    //Delete method
    public Optional<AuthMenus> delete(Long id) {
        Optional<AuthMenus> optionalMenu = authMenusRepository.findById(id);
        if (optionalMenu.isPresent()) {
            AuthMenus menu = optionalMenu.get();
//            if (menu.getNotDeleteAble()) {
//                throw new IllegalStateException("This menu cannot be deleted");
//            }
            authMenusRepository.delete(menu);
            return Optional.of(menu);
        } else {
            return Optional.empty();
        }
    }

    public List<AuthMenus> importFromXml(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(AuthMenuList.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        AuthMenuList authMenuList = (AuthMenuList) unmarshaller.unmarshal(file);

        for (AuthMenuXmlDTO authMenuXmlDTO : authMenuList.getAuthMenus()) {
            saveOrUpdateMenu(authMenuXmlDTO);
        }
        return authMenusRepository.findAll();
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
    private void saveOrUpdateMenu(AuthMenuXmlDTO dto) {
        AuthMenus existingMenu = authMenusRepository.findByMenuName(dto.getMenuName());
        if (existingMenu == null) {
            AuthMenus authMenu = new AuthMenus();
            authMenu.setName(dto.getName());
            authMenu.setDescription(dto.getDescription());
//            AuthMenus.setActive(dto.getActive());
            authMenu.setMenuName(dto.getMenuName());
            authMenu.setNotDeleteAble(dto.getNotDeleteAble());
            Set<AuthGroups> groupList = new HashSet<>(); // Use Set to avoid duplicates
            for (String groupDTO : dto.getGroups()) {
                // Find the existing group by name or other unique identifier
                Optional<AuthGroups> optionalGroup = Optional.ofNullable(authGroupsRepository.findByGroupName(groupDTO));
                if (optionalGroup.isPresent()) {
                    AuthGroups group = optionalGroup.get(); // Get the user if present
                    groupList.add(group); // Add existing group to the set
                }
            }
            // Set the group list in the group
            authMenu.setAuthGroups(groupList);

            if (dto.getParentMenuName() != null && !dto.getParentMenuName().isEmpty()) {
                AuthMenus parentMenu = authMenusRepository.findByMenuName(dto.getParentMenuName());
                if (parentMenu != null) {
                    authMenu.setParentMenuId(parentMenu);  // Add the current menu as a child to the parent menu
                    authMenusRepository.save(authMenu);  // Save the parent with the new child
                } else {
                    throw new EntityNotFoundException("Parent menu with name " + dto.getParentMenuName() + " not found");
                }
            }
            if (dto.getParentMenuName() == null || dto.getParentMenuName().isEmpty()) {
                authMenusRepository.save(authMenu);
            }
        }
    }

    @Transactional
    public void addChildMenuToParent(Long parentId, Long childId) {
        AuthMenus parentMenu = authMenusRepository.findById(parentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent menu not found"));

        AuthMenus childMenu = authMenusRepository.findById(childId)
                .orElseThrow(() -> new EntityNotFoundException("Child menu not found"));
        parentMenu.addChildMenu(childMenu);
        authMenusRepository.save(parentMenu);
    }
}
