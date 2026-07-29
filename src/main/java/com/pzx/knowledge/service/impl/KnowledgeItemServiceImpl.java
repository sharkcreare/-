package com.pzx.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.pzx.knowledge.common.exception.BusinessException;
import com.pzx.knowledge.common.result.ResultCode;
import com.pzx.knowledge.dto.KnowledgeItemDTO;
import com.pzx.knowledge.entity.ItemTag;
import com.pzx.knowledge.entity.KnowledgeItem;
import com.pzx.knowledge.mapper.ItemTagMapper;
import com.pzx.knowledge.mapper.KnowledgeItemMapper;
import com.pzx.knowledge.service.KnowledgeItemService;
import com.pzx.knowledge.utils.UserContext;
import com.pzx.knowledge.vo.KnowledgeItemVO;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class KnowledgeItemServiceImpl
        extends ServiceImpl<KnowledgeItemMapper, KnowledgeItem> implements KnowledgeItemService {


    private final ItemTagMapper itemTagMapper;

    @Override
    @Transactional
    public KnowledgeItemVO create(KnowledgeItemDTO dto) {
        Long userId = UserContext.getUser();

         KnowledgeItem  item= new KnowledgeItem();
         item.setUserId(userId);
         BeanUtils.copyProperties(dto,item);
         item.setSourceUrl(dto.getSourceUrl() !=null ? dto.getSourceUrl() : "");
         item.setSummary(dto.getSummary()!=null ? dto.getSummary() : "");
         item.setIsTop(dto.getIsTop() !=null&& dto.getIsTop() ? 1: 0  );
         this.save(item);
        return toVo(item);
    }



    @Override
    @Transactional
    public KnowledgeItemVO update(Long itemId,KnowledgeItemDTO dto) {
        Long userId = UserContext.getUser();
        KnowledgeItem item =this.getById(itemId);

        if(item ==null || !item.getUserId().equals(userId)){
            throw  new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }
        item.setTitle(dto.getTitle());
        item.setContent(dto.getContent());
        item.setContentType(dto.getContentType());
        item.setSummary(dto.getSummary()!=null ? dto.getSummary():"");
        item.setSourceUrl(dto.getSourceUrl()!=null ? dto.getSourceUrl():"");
        item.setIsTop(dto.getIsTop() !=null && dto.getIsTop() ?1:0);
        this.updateById(item);
        return toVo(item);
    }

    @Override
    public void delete(Long id) {
        Long userId =UserContext.getUser();
        if (id==null){
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }
        KnowledgeItem item =this.getById(id);
        if (item==null||!item.getId().equals(userId))
        {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }
        LambdaQueryWrapper<ItemTag> wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(ItemTag::getItemId,id);
        itemTagMapper.delete(wrapper);

        this.removeById(id);
    }

    @Override
    public Page<KnowledgeItemVO> getPage(Integer pageNum, Integer pageSize, String contentType, Long tagId, String keyword) {
        Long userId =UserContext.getUser();
        LambdaQueryWrapper<KnowledgeItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeItem::getUserId,userId);
        if (StringUtils.hasText(contentType)){
            wrapper.eq(KnowledgeItem::getContentType,contentType);
        }
        if(StringUtils.hasText(keyword)){
            wrapper.and(w->w.like(KnowledgeItem::getTitle,keyword)
                    .or().like(KnowledgeItem::getSourceUrl,keyword)
                    .or().like(KnowledgeItem::getContent,keyword));
        }
        if (tagId !=null){
            LambdaQueryWrapper<ItemTag> itWrapper=new LambdaQueryWrapper<>();
            itWrapper.eq(ItemTag::getTagId,tagId);
            List<Long> itemIds =itemTagMapper.selectList(itWrapper)
                    .stream().map(ItemTag::getItemId)
                    .collect(Collectors.toList());

            if(itemIds.isEmpty()){
                Page<KnowledgeItemVO> emptyPage =new Page<>(pageNum,pageSize);
                emptyPage.setRecords(Collections.emptyList());
                emptyPage.setTotal(0);
                return  emptyPage;
                            }
            wrapper.in(KnowledgeItem::getId,itemIds);

        }


        wrapper.orderByDesc(KnowledgeItem::getIsTop)
                .orderByDesc(KnowledgeItem::getUpdatedAt);
        Page<KnowledgeItem>page =new Page<>(pageNum,pageSize);
        Page<KnowledgeItem>result= this.page(page,wrapper);
        List<KnowledgeItemVO> voList=result.getRecords().stream()
                .map(this::toVo).collect(Collectors.toList());
        Page<KnowledgeItemVO> vopage =new Page<>(pageNum,pageSize);
        vopage.setTotal(result.getTotal());
        vopage.setRecords(voList);
        vopage.setPages(result.getPages());
        return vopage;
    }

    @Override
    public KnowledgeItemVO getDetail(Long id) {
        return null;
    }

    @Override
    public void toggleTop(Long id, Boolean isTop) {

    }

    //私有方法：将数据转成VO
    private KnowledgeItemVO toVo (KnowledgeItem item){

        KnowledgeItemVO vo =new KnowledgeItemVO();
        Long userId=UserContext.getUser();
        vo.setUserid(userId);
        BeanUtils.copyProperties(item ,vo);

        vo.setIsFavorite(item.getIsFavorite()==1);

        vo.setIsTop(item.getIsTop() == 1);

     return vo;
    }
}
