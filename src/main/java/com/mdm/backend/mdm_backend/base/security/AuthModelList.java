package com.mdm.backend.mdm_backend.base.security;

import com.mdm.backend.mdm_backend.base.DTO.models.AuthModelDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "models")
public class AuthModelList {

    private List<AuthModelDTO> authModels;

    @XmlElement(name = "authModel")
    public List<AuthModelDTO> getAuthModels() {
        return authModels;
    }

    public void setAuthModels(List<AuthModelDTO> authModels) {
        this.authModels = authModels;
    }
}
