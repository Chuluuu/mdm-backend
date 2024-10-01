package com.mdm.backend.mdm_backend.base.repository;

import com.mdm.backend.mdm_backend.base.models.AuthModels;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthModelsRepository extends JpaRepository<AuthModels, Long> {
    AuthModels findByModelName(String modelName);
}

