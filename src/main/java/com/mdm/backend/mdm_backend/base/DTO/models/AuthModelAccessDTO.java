package com.mdm.backend.mdm_backend.base.DTO.models;

import jakarta.xml.bind.annotation.XmlElement;

public  class AuthModelAccessDTO {
    private String name ;
    private String groupId;
    private Boolean permRead;
    private Boolean permCreate;
    private Boolean permUpdate;
    private Boolean permDelete;

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name){this.name = name; }
    @XmlElement
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @XmlElement
    public Boolean getPermRead() {
        return permRead;
    }

    public void setPermRead(Boolean permRead) {
        this.permRead = permRead;
    }

    @XmlElement
    public Boolean getPermCreate() {
        return permCreate;
    }

    public void setPermCreate(Boolean permCreate) {
        this.permCreate = permCreate;
    }

    @XmlElement
    public Boolean getPermUpdate() {
        return permUpdate;
    }

    public void setPermUpdate(Boolean permUpdate) {
        this.permUpdate = permUpdate;
    }

    @XmlElement
    public Boolean getPermDelete() {
        return permDelete;
    }

    public void setPermDelete(Boolean permDelete) {
        this.permDelete = permDelete;
    }
}
