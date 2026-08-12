package com.pzx.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.pzx.knowledge.entity.Favorite;
import com.pzx.knowledge.mapper.FavoriteMapper;
import com.pzx.knowledge.service.FavoriteService;
import com.pzx.knowledge.service.KnowledgeItemService;
import com.pzx.knowledge.utils.UserContext;
import com.pzx.knowledge.vo.KnowledgeItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite>
        implements FavoriteService {


    private final FavoriteMapper favoriteMapper;
    private final KnowledgeItemService knowledgeItemService;

    @Override
    @Transactional
    public void add( Long itemId) {
        Long userId = UserContext.getUser();
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getItemId, itemId);

          if(favoriteMapper.selectCount(wrapper)>0){
              return;
          }
          Favorite fav=new Favorite();
          fav.setUserId(userId);
          fav.setItemId(itemId);
          favoriteMapper.insert(fav);

    }

    @Override
    @Transactional
    public void remove( Long itemId) {
        Long userId = UserContext.getUser();
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId).eq(Favorite::getItemId, itemId);
        favoriteMapper.delete(wrapper);

    }

    @Override
    public Page<KnowledgeItemVO> pageList( Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUser();
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreatedAt);
        IPage<Favorite> favPage = favoriteMapper.selectPage(new Page<>(pageNum, pageSize),wrapper);

        List<Long> itemIds = favPage.getRecords().stream()
                .map(Favorite::getItemId)
                .toList();
        List<KnowledgeItemVO> vos =itemIds.isEmpty()
                ? Collections.emptyList()
                :knowledgeItemService.listByIdsForUser(itemIds);


        Map<Long, KnowledgeItemVO> map = vos.stream()
                .collect(Collectors.toMap(KnowledgeItemVO::getId, v -> v, (a, b) -> a, LinkedHashMap::new));
        List<KnowledgeItemVO> ordered = itemIds.stream()
                .map(map::get).filter(v -> v != null).collect(Collectors.toList());

        Page<KnowledgeItemVO> result = new Page<>(pageNum, pageSize, favPage.getTotal());
        result.setRecords(ordered);
        result.setPages(favPage.getPages());
        return result;
    }
}
