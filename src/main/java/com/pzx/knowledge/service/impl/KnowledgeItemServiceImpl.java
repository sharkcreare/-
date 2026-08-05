package com.pzx.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.pzx.knowledge.common.exception.BusinessException;
import com.pzx.knowledge.common.result.ResultCode;
import com.pzx.knowledge.dto.KnowledgeItemDTO;
import com.pzx.knowledge.entity.ItemTag;
import com.pzx.knowledge.entity.KnowledgeItem;
import com.pzx.knowledge.entity.Tag;
import com.pzx.knowledge.mapper.FavoriteMapper;
import com.pzx.knowledge.mapper.ItemTagMapper;
import com.pzx.knowledge.mapper.KnowledgeItemMapper;
import com.pzx.knowledge.mapper.TagMapper;
import com.pzx.knowledge.service.KnowledgeItemService;

import com.pzx.knowledge.utils.UserContext;
import com.pzx.knowledge.vo.KnowledgeItemVO;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.pzx.knowledge.entity.Favorite;
import com.pzx.knowledge.mapper.FavoriteMapper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class KnowledgeItemServiceImpl
        extends ServiceImpl<KnowledgeItemMapper, KnowledgeItem> implements KnowledgeItemService {
    private final FavoriteMapper favoriteMapper;
    private final TagMapper tagMapper;
    private final ItemTagMapper itemTagMapper;

    @Override
    @Transactional
    public KnowledgeItemVO create(KnowledgeItemDTO dto) {
         Long userId = UserContext.getUser();
//        检验标签
        List<Long> distinctTagIds = getDistinctTagIds(dto.getTagIds());
        validateTags(distinctTagIds, userId);

         KnowledgeItem  item= new KnowledgeItem();
         item.setUserId(userId);
         BeanUtils.copyProperties(dto,item);
         item.setSourceUrl(dto.getSourceUrl() !=null ? dto.getSourceUrl() : "");
         item.setSummary(dto.getSummary()!=null ? dto.getSummary() : "");
         item.setIsTop(dto.getIsTop() !=null&& dto.getIsTop() ? 1: 0  );
         this.save(item);

        saveItemTags(item.getId(), distinctTagIds);

         KnowledgeItemVO vo = toVo(item);
         fillSingleTags(vo);
        return vo;
    }



    @Override
    @Transactional
    public KnowledgeItemVO update(Long itemId,KnowledgeItemDTO dto) {
        Long userId = UserContext.getUser();
        KnowledgeItem item =this.getById(itemId);

        if(item ==null || !item.getUserId().equals(userId)){
            throw  new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }
        List<Long> distinctTagIds = getDistinctTagIds(dto.getTagIds());
        validateTags( distinctTagIds, userId);

        item.setTitle(dto.getTitle());
        item.setContent(dto.getContent());
        item.setContentType(dto.getContentType());
        item.setSummary(dto.getSummary()!=null ? dto.getSummary():"");
        item.setSourceUrl(dto.getSourceUrl()!=null ? dto.getSourceUrl():"");
        item.setIsTop(dto.getIsTop() !=null && dto.getIsTop() ?1:0);
        this.updateById(item);

        // 先删旧关联，再插新关联
        itemTagMapper.delete(new LambdaQueryWrapper<ItemTag>()
                .eq(ItemTag::getItemId,itemId));
            saveItemTags(itemId,dto.getTagIds());

            KnowledgeItemVO vo=toVo(item);

        fillSingleTags(vo);
        return vo;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Long userId =UserContext.getUser();
        if (id==null){
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }
        KnowledgeItem item =this.getById(id);
        if (item==null||!item.getUserId().equals(userId))
        {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }
        itemTagMapper.delete(new LambdaQueryWrapper<ItemTag>()
                .eq(ItemTag::getItemId,id));
        favoriteMapper.delete(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getItemId,id)
                .eq(Favorite::getUserId,userId));


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

        batchFillTags(voList);

        Page<KnowledgeItemVO> vopage =new Page<>(pageNum,pageSize);
        vopage.setTotal(result.getTotal());
        vopage.setPages(result.getPages());
        vopage.setCurrent(result.getCurrent());
        vopage.setSize(result.getSize());
        vopage.setRecords(voList);
        return vopage;
    }

    @Override
    public KnowledgeItemVO getDetail(Long id) {
        Long userId = UserContext.getUser();
        KnowledgeItem item = this.getById(id);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }
        baseMapper.incrementViewCount(id);
        boolean isFavorite = favoriteMapper.exists(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getItemId, id)
                .eq(Favorite::getUserId, userId));

        KnowledgeItemVO vo = toVo(item);
        vo.setViewCount(item.getViewCount() + 1); // 返回给前端的是+1后的值
        vo.setIsFavorite(isFavorite);
        fillSingleTags(vo);
        return vo;

    }


    @Override
    public void toggleTop(Long id, Boolean isTop) {
            Long userId =UserContext.getUser();
            KnowledgeItem item =this.getById(id);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.ITEM_NOT_FOUND);
        }
        item.setIsTop(isTop !=null&& isTop?1:0);
        this.updateById(item);
    }




    //私有方法：将数据转成VO
    private KnowledgeItemVO toVo (KnowledgeItem item){

        KnowledgeItemVO vo =new KnowledgeItemVO();
        BeanUtils.copyProperties(item ,vo);

        vo.setIsFavorite(item.getIsFavorite() != null && item.getIsFavorite() == 1);

        vo.setIsTop(item.getIsTop() != null && item.getIsTop() == 1);

     return vo;
    }
//    去重和过滤null
    private List<Long> getDistinctTagIds(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptyList();
        }
        return tagIds.stream().distinct().filter(java.util.Objects::nonNull).toList();
    }
//    保存【笔记 - 标签关联关系】
    private void saveItemTags(Long itemId, List<Long> tagIds) {
        // 没有标签，直接返回
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }
        // 循环每个标签ID，向中间表 item_tag 插入记录
        for (Long tagId : tagIds) {
            ItemTag it = new ItemTag();
            it.setItemId(itemId); //笔记id
            it.setTagId(tagId);   //标签id
            itemTagMapper.insert(it);
        }
    }

//    validateTags 校验标签
    private void validateTags(List<Long> tagIds, Long userId) {
        // 如果前端没传标签，直接放行，不用校验
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }
        // 根据传入的标签id集合，批量查询标签
        List<Tag> tags = tagMapper.selectByIds(tagIds);

        // 两个条件：
        // ① 查询出来标签数量 = 前端传来id数量 → 不存在无效id（有id在数据库找不到）
        // ② 所有标签的创建人 == 当前登录用户userId
        boolean allOwned = tags.size() == tagIds.size()
                && tags.stream().allMatch(t -> t.getUserId().equals(userId));

        // 只要不满足上面条件：标签不存在 / 使用了别人的标签 → 报错
        if (!allOwned) {
            throw new BusinessException(ResultCode.TAG_NOT_FOUND);
        }
    }

//    查询填充标签名称（VO 组装）
    private void fillSingleTags(KnowledgeItemVO vo) {
        // 根据笔记id 查询中间表item_tag所有关联记录
        List<ItemTag> mappings = itemTagMapper.selectList(
                new LambdaQueryWrapper<ItemTag>().eq(ItemTag::getItemId, vo.getId()));

        // 如果这条笔记没有绑定任何标签，前端tags设置空集合
        if (mappings.isEmpty()) {
            vo.setTags(Collections.emptyList());
            return;
        }

        // 提取所有标签id
        List<Long> tagIds = mappings.stream().map(ItemTag::getTagId).toList();
        // 批量查询标签，构建 map<标签id,标签名称>
        Map<Long, String> tagMap = tagMapper.selectBatchIds(tagIds).stream()
                .collect(Collectors.toMap(Tag::getId, Tag::getName));

        // 根据中间表的tagId，取出标签名字，组装 List<String> 标签名集合
        vo.setTags(mappings.stream()
                .map(m -> tagMap.get(m.getTagId()))
                .collect(Collectors.toList()));
    }
    private void batchFillTags (List<KnowledgeItemVO> voList){
        if(voList.isEmpty()){
            return;
        }
        List<Long> itemIds =voList.stream()
                .map(KnowledgeItemVO::getId)
                .toList();

        List<ItemTag>allMappings =itemTagMapper.selectList(
                new LambdaQueryWrapper<ItemTag>()
                        .in(ItemTag::getItemId,itemIds)
        );
        List<Long> allTagIds =allMappings.stream()
                .map(ItemTag::getTagId)
                .distinct()
                .toList();
        if(allTagIds.isEmpty()){
            voList.forEach(vo->vo.setTags(Collections.emptyList()));
            return;
        }

        Map<Long,String>tagMap=tagMapper.selectList(
                new LambdaQueryWrapper<Tag>().in(Tag::getId, allTagIds)
        ).stream().collect(Collectors.toMap(Tag::getId,Tag::getName));

        Map<Long,List<String>>grouped =allMappings.stream()
                .collect(Collectors.groupingBy(ItemTag::getItemId,Collectors.mapping(
                     m->tagMap.get(m.getTagId()),Collectors.toList())
                ));

        voList.forEach(vo-> vo.setTags(grouped.getOrDefault(vo.getId(),Collections.emptyList())));

    }
}
