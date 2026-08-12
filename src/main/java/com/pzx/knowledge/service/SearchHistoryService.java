package com.pzx.knowledge.service;

import com.pzx.knowledge.vo.SearchHistoryVO;
import java.util.List;

public interface SearchHistoryService {

    void saveKeyword(String keyword);

    List<SearchHistoryVO> listRecent( int limit);

    void clearAll();
}