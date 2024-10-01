package com.mdm.backend.mdm_backend.base.DTO.models;

import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

public class AuthModelDTO {
    private String name;
    private String description;
    private String modelName;
    private Boolean active;
    private List<AuthModelAccessDTO> access;

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
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @XmlElement
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @XmlElement(name = "access")
    public List<AuthModelAccessDTO> getAccess() {
        return access;
    }

    public void setAccess(List<AuthModelAccessDTO> access) {
        this.access = access;
    }
}

