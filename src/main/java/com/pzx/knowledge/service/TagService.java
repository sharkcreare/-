package com.pzx.knowledge.service;

import com.pzx.knowledge.dto.TagDTO;
import com.pzx.knowledge.vo.TagVO;

import java.util.List;

/**
 * 标签服务接口 —— 统一返回 VO，不把 Entity 泄露到上层
 */
public interface TagService {

    List<TagVO> listByUser(Long userId);

    /** 创建标签，直接返回创建好的 TagVO（Controller 不再二次查询） */
    TagVO create(Long userId, TagDTO dto);

    /** 更新标签，返回更新后的 TagVO（与 create 返回约定一致） */
    TagVO update(Long id, Long userId, TagDTO dto);

    void delete(Long id, Long userId);
}