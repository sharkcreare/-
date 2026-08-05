package com.pzx.knowledge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzx.knowledge.entity.KnowledgeItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface KnowledgeItemMapper extends BaseMapper<KnowledgeItem> {
    @Update("UPDATE knowledge_item SET view_count = view_count + 1 WHERE id = #{id}")
    void incrementViewCount(@Param("id") Long id);
}