package com.pzx.knowledge.repository;

import com.pzx.knowledge.document.KnowledgeItemDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeItemRepository extends ElasticsearchRepository<KnowledgeItemDocument, Long> {

    /** 全文搜索（title/content/summary 多字段匹配） */
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title\", \"content\", \"summary\"]}}")
    Page<KnowledgeItemDocument> searchByKeyword(String keyword, Pageable pageable);

    /** 指定用户搜索 */
    @Query("{\"bool\": {\"must\": [{\"term\": {\"userId\": ?0}}, {\"multi_match\": {\"query\": \"?1\", \"fields\": [\"title\", \"content\", \"summary\"]}}]}}")
    Page<KnowledgeItemDocument> searchByUserIdAndKeyword(Long userId, String keyword, Pageable pageable);
}