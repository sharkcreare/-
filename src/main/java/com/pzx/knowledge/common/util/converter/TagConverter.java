package com.pzx.knowledge.common.util.converter;

import com.pzx.knowledge.entity.KnowledgeItem;
import com.pzx.knowledge.vo.KnowledgeItemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class TagConverter {
    //私有方法：将数据转成VO
   KnowledgeItemVO toVo (KnowledgeItem item){

        KnowledgeItemVO vo =new KnowledgeItemVO();
        BeanUtils.copyProperties(item ,vo);

        vo.setIsFavorite(item.getIsFavorite() != null && item.getIsFavorite() == 1);

        vo.setIsTop(item.getIsTop() != null && item.getIsTop() == 1);

        return vo;
    }
}
