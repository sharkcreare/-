package com.pzx.knowledge.mapstruct;

import com.pzx.knowledge.entity.Tag;
import com.pzx.knowledge.vo.TagVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagConverter {

    @Mapping(target = "itemCount", ignore = true)
    TagVO toVO(Tag tag);
}