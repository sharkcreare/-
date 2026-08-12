package com.pzx.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.pzx.knowledge.entity.SearchHistory;
import com.pzx.knowledge.mapper.SearchHistoryMapper;
import com.pzx.knowledge.service.SearchHistoryService;
import com.pzx.knowledge.utils.UserContext;
import com.pzx.knowledge.vo.SearchHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl
        extends ServiceImpl<SearchHistoryMapper, SearchHistory>
        implements SearchHistoryService {
    @Override
    @Transactional
    public void saveKeyword(String keyword) {
        Long userId = UserContext.getUser();
      lambdaUpdate()
              .eq(SearchHistory::getUserId, userId)
              .eq(SearchHistory::getKeyword, keyword)
              .remove();
      SearchHistory history = new SearchHistory();
      history.setUserId(userId);
      history.setKeyword(keyword);
      save(history);
    }

    @Override
    public List<SearchHistoryVO> listRecent(int limit) {
        Long userId = UserContext.getUser();
       return lambdaQuery()
                .eq(SearchHistory::getUserId, userId)
                .orderByDesc(SearchHistory::getCreatedAt)
                .last("LIMIT " + limit)
                .list()
                .stream()
                .map(this::toVo)
                .collect(Collectors.toList());
    }

    @Override
    public void clearAll() {
        Long userId = UserContext.getUser();
        lambdaUpdate()
        .eq(SearchHistory::getUserId, userId)
        .remove();

    }
    private SearchHistoryVO toVo(SearchHistory entity) {
        SearchHistoryVO vo = new SearchHistoryVO();
        vo.setId(entity.getId());
        vo.setKeyword(entity.getKeyword());
        vo.setCreatedAt(entity.getCreatedAt());
        return vo;
    }
}
