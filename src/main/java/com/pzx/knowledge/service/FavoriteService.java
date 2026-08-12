package com.pzx.knowledge.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzx.knowledge.vo.KnowledgeItemVO;

public interface FavoriteService {

    void add(Long itemId);

    void remove(Long itemId);

    Page<KnowledgeItemVO> pageList(Integer pageNum, Integer pageSize);
}