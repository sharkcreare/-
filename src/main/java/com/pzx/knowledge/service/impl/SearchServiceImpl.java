package com.pzx.knowledge.service.impl;

import com.pzx.knowledge.document.KnowledgeItemDocument;
import com.pzx.knowledge.repository.KnowledgeItemRepository;
import com.pzx.knowledge.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final KnowledgeItemRepository knowledgeItemRepository;

    @Override
    public Page<KnowledgeItemDocument> search(String keyword, int pageNum, int pageSize) {

        return knowledgeItemRepository.searchByKeyword(keyword, PageRequest.of(pageNum-1, pageSize));
    }

    @Override
    public Page<KnowledgeItemDocument> searchByUser(Long userId, String keyword, int pageNum, int pageSize) {
        return knowledgeItemRepository.searchByUserIdAndKeyword(userId, keyword, PageRequest.of(pageNum-1, pageSize));
    }

    @Override
    public List<String> suggest(String prefix) {
        return Collections.emptyList();
    }

    @Override
    public void syncToEs(KnowledgeItemDocument document) {
        knowledgeItemRepository.save(document);
        log.info("同步到ES成功：id={}",document.getId());
    }

    @Override
    public void deleteFromEs(Long id) {
        knowledgeItemRepository.deleteById(id);
        log.info("从ES删除成功：id={}",id);
    }
}
