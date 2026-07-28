package com.pzx.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzx.knowledge.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}