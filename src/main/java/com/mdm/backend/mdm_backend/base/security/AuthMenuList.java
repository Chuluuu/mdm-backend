package com.mdm.backend.mdm_backend.base.security;

import com.mdm.backend.mdm_backend.base.DTO.menus.AuthMenuXmlDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "models")
public class AuthMenuList {
    private List<AuthMenuXmlDTO> authMenus;

    @XmlElement(name = "authMenu")
    public List<AuthMenuXmlDTO> getAuthMenus() {
        return authMenus;
    }

    public void setAuthMenus(List<AuthMenuXmlDTO> authMenus) {
        this.authMenus = authMenus;
    }
}
