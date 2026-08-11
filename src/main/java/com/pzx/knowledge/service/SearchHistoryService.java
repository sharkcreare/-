package com.pzx.knowledge.service;

import com.pzx.knowledge.vo.SearchHistoryVO;
import java.util.List;

public interface SearchHistoryService {

    void saveKeyword(Long userId, String keyword);

    List<SearchHistoryVO> listRecent(Long userId, int limit);

    void clearAll(Long userId);
}