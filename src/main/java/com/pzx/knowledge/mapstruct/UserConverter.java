package com.pzx.knowledge.mapstruct;

import com.pzx.knowledge.dto.RegisterDTO;
import com.pzx.knowledge.entity.User;
import com.pzx.knowledge.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring" ,unmappedTargetPolicy = ReportingPolicy.IGNORE)

    public interface UserConverter {

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "password", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        User toEntity(RegisterDTO dto);

        UserVO toVO(User user);
    }

