package com.pzx.knowledge.mapstruct;

import com.pzx.knowledge.dto.KnowledgeItemDTO;
import com.pzx.knowledge.entity.KnowledgeItem;
import com.pzx.knowledge.vo.KnowledgeItemVO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KnowledgeItemConverter {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "isFavorite", ignore = true)
    @Mapping(target = "isTop", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    KnowledgeItem toEntity(KnowledgeItemDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "isFavorite", ignore = true)
    @Mapping(target = "isTop", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget KnowledgeItem entity, KnowledgeItemDTO dto);

    @Mapping(target = "isFavorite", expression = "java(entity.getIsFavorite() != null && entity.getIsFavorite() == 1)")
    @Mapping(target = "isTop", expression = "java(entity.getIsTop() != null && entity.getIsTop() == 1)")
    @Mapping(target = "tags", ignore = true)
    KnowledgeItemVO toVO(KnowledgeItem entity);
}