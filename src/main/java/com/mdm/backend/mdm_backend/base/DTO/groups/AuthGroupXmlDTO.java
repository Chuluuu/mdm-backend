package com.mdm.backend.mdm_backend.base.DTO.groups;

import com.mdm.backend.mdm_backend.base.DTO.models.AuthModelAccessDTO;
import jakarta.xml.bind.annotation.XmlElement;

import java.awt.*;
import java.util.List;

public class AuthGroupXmlDTO {
    private String name;
    private String description;
    private Boolean notDeleteAble;
    private Boolean active;
    private String groupName;
    private List<AuthGroupUsersXmlDTO> users;

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
    @XmlElement(name = "users")
    public List<AuthGroupUsersXmlDTO> getUsers() {
        return users;
    }

    public void setUsers(List<AuthGroupUsersXmlDTO> users) {
        this.users = users;
    }
    @XmlElement
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
