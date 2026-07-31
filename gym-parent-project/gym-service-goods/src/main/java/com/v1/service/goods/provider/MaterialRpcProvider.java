package com.v1.service.goods.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.equipment.MaterialDTO;
import com.v1.api.equipment.MaterialRpcService;
import com.v1.service.goods.equipment.entity.ListParam;
import com.v1.service.goods.equipment.entity.Material;
import com.v1.service.goods.equipment.service.MaterialService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@DubboService
public class MaterialRpcProvider implements MaterialRpcService {

    @Autowired
    private MaterialService materialService;

    @Override
    public PageResultDTO<MaterialDTO> listMaterials(PageDTO page, String name) {
        ListParam param = new ListParam();
        param.setCurrentPage(page.getCurrentPage().intValue());
        param.setPageSize(page.getPageSize().intValue());
        param.setName(name);

        IPage<Material> result = materialService.list(param);

        PageResultDTO<MaterialDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            MaterialDTO materialDTO = new MaterialDTO();
            BeanUtils.copyProperties(entity, materialDTO);
            return materialDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public MaterialDTO getMaterialById(Long id) {
        Material entity = materialService.getById(id);
        if (entity == null) {
            return null;
        }
        MaterialDTO dto = new MaterialDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public void addMaterial(MaterialDTO material) {
        Material entity = new Material();
        BeanUtils.copyProperties(material, entity);
        materialService.save(entity);
    }

    @Override
    public void updateMaterial(MaterialDTO material) {
        Material entity = new Material();
        BeanUtils.copyProperties(material, entity);
        materialService.updateById(entity);
    }

    @Override
    public void deleteMaterial(Long id) {
        materialService.removeById(id);
    }

    @Override
    public int count() {
        return (int) materialService.count();
    }
}
