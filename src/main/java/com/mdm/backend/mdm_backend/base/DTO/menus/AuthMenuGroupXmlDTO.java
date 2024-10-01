package com.mdm.backend.mdm_backend.base.DTO.menus;

import jakarta.xml.bind.annotation.XmlElement;

public class AuthMenuGroupXmlDTO {
    private String groupId;

    @XmlElement(name = "groupId")
    public String getGroupId() {return groupId;}
    public void setGroupId(String groupId){this.groupId = groupId; }
}
