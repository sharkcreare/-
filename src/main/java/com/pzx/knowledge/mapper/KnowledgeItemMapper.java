package com.pzx.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzx.knowledge.entity.KnowledgeItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KnowledgeItemMapper extends BaseMapper<KnowledgeItem> {
}