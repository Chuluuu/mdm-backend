package com.mdm.backend.mdm_backend.base.DTO.groups;

import jakarta.xml.bind.annotation.XmlElement;

public class AuthGroupUsersXmlDTO {
    private Long userId;

    @XmlElement(name = "userId")
    public Long getUserId() {return userId;}
    public void setUserId(Long userId){this.userId = userId; }
}
