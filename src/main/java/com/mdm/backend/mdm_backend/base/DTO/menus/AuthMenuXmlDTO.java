package com.mdm.backend.mdm_backend.base.DTO.menus;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;
@XmlRootElement(name = "authMenu")
public class AuthMenuXmlDTO {
    private String name;
    private String description;
    private Boolean notDeleteAble;
    private Boolean active;
    private String menuName;
    private String parentMenuName;
    private List<String> groups;

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @XmlElement
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    @XmlElement
    public Boolean getNotDeleteAble() {
        return notDeleteAble;
    }

    public void setNotDeleteAble(Boolean notDeleteAble) {
        this.notDeleteAble = notDeleteAble;
    }
    @XmlElementWrapper(name = "groups") // Wrap the list of groups
    @XmlElement(name = "groupId") // Individual group element
    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    public String getParentMenuName() {
        return parentMenuName;
    }

    public void setParentMenuName(String parentMenuName) {
        this.parentMenuName = parentMenuName;
    }
}
