package com.pzx.knowledge.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.IService;
import com.pzx.knowledge.dto.KnowledgeItemDTO;
import com.pzx.knowledge.entity.KnowledgeItem;
import com.pzx.knowledge.vo.KnowledgeItemVO;

public interface KnowledgeItemService extends IService<KnowledgeItem> {

    KnowledgeItemVO create(KnowledgeItemDTO dto);

    KnowledgeItemVO update(Long itemId, KnowledgeItemDTO dto);

    void delete(Long id);

    Page<KnowledgeItemVO> getPage(Integer pageNum, Integer pageSize,
                                  String contentType, Long tagId, String keyword);

    KnowledgeItemVO getDetail(Long id);

    void toggleTop(Long id, Boolean isTop);
}