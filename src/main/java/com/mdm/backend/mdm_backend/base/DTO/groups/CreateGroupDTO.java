package com.mdm.backend.mdm_backend.base.DTO.groups;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupDTO {
    private String name;
    private String description;
    private Set<Long> userIds;

}
