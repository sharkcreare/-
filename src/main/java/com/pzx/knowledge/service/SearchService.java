package com.pzx.knowledge.service;

import com.pzx.knowledge.document.KnowledgeItemDocument;
import org.springframework.data.domain.Page;

import java.util.List;

/** 全文搜索服务接口 */
public interface SearchService {

    Page<KnowledgeItemDocument> search(String keyword, int pageNum, int pageSize);

    Page<KnowledgeItemDocument> searchByUser(Long userId, String keyword, int pageNum, int pageSize);

    List<String> suggest(String prefix);

    void syncToEs(KnowledgeItemDocument document);

    void deleteFromEs(Long id);
}