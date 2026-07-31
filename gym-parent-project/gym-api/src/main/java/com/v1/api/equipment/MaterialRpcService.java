package com.v1.api.equipment;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.equipment.MaterialDTO;

public interface MaterialRpcService {
    PageResultDTO<MaterialDTO> listMaterials(PageDTO page, String name);

    MaterialDTO getMaterialById(Long id);

    void addMaterial(MaterialDTO material);

    void updateMaterial(MaterialDTO material);

    void deleteMaterial(Long id);
}
