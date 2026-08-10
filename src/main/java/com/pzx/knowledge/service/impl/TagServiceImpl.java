package com.pzx.knowledge.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pzx.knowledge.common.exception.BusinessException;
import com.pzx.knowledge.common.result.ResultCode;
import com.pzx.knowledge.entity.ItemTag;
import com.pzx.knowledge.mapper.ItemTagMapper;
import com.pzx.knowledge.mapper.TagMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pzx.knowledge.dto.TagDTO;
import com.pzx.knowledge.entity.Tag;
import com.pzx.knowledge.service.TagService;
import com.pzx.knowledge.vo.TagVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService  {

    private final ItemTagMapper itemTagMapper;

    @Override
    @Cacheable(cacheNames = "tagList",key = "#userId")
    public List<TagVO> listByUser(Long userId) {

        List<Tag> tags= lambdaQuery()
                .eq(Tag::getUserId,userId)
                .orderByDesc(Tag::getUpdatedAt)
                .list();
        if(tags==null|| tags.isEmpty()){
            return Collections.emptyList();

        }
        QueryWrapper<ItemTag> countWrapper = new QueryWrapper<>();
        countWrapper.select("tag_id","count(*)as cnt")
                    .in("tag_id",tags.stream().map(Tag::getId).collect(Collectors.toList()))
                    .groupBy("tag_id");
        Map<Long,Long> countMap=itemTagMapper.selectMaps(countWrapper).stream()
                .collect(Collectors.toMap(
                        m->((Number)m.get("tag_id")).longValue(),
                        m->((Number)m.get("cnt")).longValue(),
                        (a,b)->a));

                return tags.stream().map(tag->ToVo(tag,countMap.getOrDefault(tag.getId(),0L)))
                        .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "tagList",key = "#userId")
    public TagVO create(Long userId, TagDTO dto) {
        LambdaQueryWrapper <Tag> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(Tag::getUserId,userId)
                .eq(Tag::getName,dto.getName());
        if(this.count(wrapper)>0){
            throw new BusinessException(ResultCode.TAG_EXISTS);
        }
        Tag tag =new Tag();
        tag.setName(dto.getName());
        tag.setUserId(userId);
        tag.setColor(dto.getColor()!=null?dto.getColor():"#1677ff");
        this.save(tag);
        return ToVo(tag,0L);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "tagList",key = "#userId")
    public TagVO update(Long id, Long userId, TagDTO dto) {
                    Tag tag =this.getById(id);
                    if(tag==null||!tag.getUserId().equals(userId)){
                        throw new BusinessException(ResultCode.TAG_NOT_FOUND);
                    }
                    LambdaQueryWrapper <Tag> wrapper =new LambdaQueryWrapper<>();
                    wrapper.eq(Tag::getUserId,userId)
                           .eq(Tag::getName,dto.getName())
                            .ne(Tag::getId,id);
                    if(this.count(wrapper)>0){
                        throw new BusinessException(ResultCode.TAG_EXISTS);

                    }
                    tag.setName(dto.getName());
                    tag.setColor(dto.getColor()!=null? dto.getColor() : tag.getColor());
                    this.updateById(tag);
                    return ToVo(tag,countByTagId(id));

    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "tagList",key ="#userId")
    public void delete(Long id, Long userId) {
            Tag tag =this.getById(id);
            if (tag == null||!tag.getUserId().equals(userId)) {
                throw new BusinessException(ResultCode.TAG_NOT_FOUND);
            }
            LambdaQueryWrapper<ItemTag> Itwrapper =new LambdaQueryWrapper<>();
            Itwrapper.eq(ItemTag::getTagId,id);
            itemTagMapper.delete(Itwrapper);
                    this.removeById(id);
    }





    private Long countByTagId(Long tagId) {
        LambdaQueryWrapper <ItemTag> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(ItemTag::getTagId,tagId);
        return itemTagMapper.selectCount(wrapper);
    }
    private TagVO ToVo (Tag tag ,long itemCount){
         TagVO vo= new TagVO();
        vo.setId(tag.getId());
        vo.setUserId(tag.getUserId());
        vo.setName(tag.getName());
        vo.setColor(tag.getColor());
        vo.setItemCount(itemCount);
        vo.setCreatedAt(tag.getCreatedAt());
        vo.setUpdatedAt(tag.getUpdatedAt());
        return vo;
    }
}
