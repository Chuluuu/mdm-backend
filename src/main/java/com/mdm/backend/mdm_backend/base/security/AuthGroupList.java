package com.mdm.backend.mdm_backend.base.security;
import com.mdm.backend.mdm_backend.base.DTO.groups.AuthGroupXmlDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "models")
public class AuthGroupList {
    private List<AuthGroupXmlDTO> authGroups;

    @XmlElement(name = "authGroup")
    public List<AuthGroupXmlDTO> getAuthGroups() {
        return authGroups;
    }

    public void setAuthGroups(List<AuthGroupXmlDTO> authGroups) {
        this.authGroups = authGroups;
    }
}
