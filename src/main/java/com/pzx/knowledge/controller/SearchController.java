
package com.pzx.knowledge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzx.knowledge.common.result.Result;
import com.pzx.knowledge.dto.KnowledgeSearchDTO;
import com.pzx.knowledge.service.KnowledgeItemService;
import com.pzx.knowledge.service.SearchHistoryService;
import com.pzx.knowledge.utils.UserContext;

import com.pzx.knowledge.vo.KnowledgeItemVO;
import com.pzx.knowledge.vo.SearchHistoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "搜索管理")
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final KnowledgeItemService knowledgeItemService;
    private final SearchHistoryService searchHistoryService;

    @Operation(summary = "搜索知识条目")
    @GetMapping
    public Result<Page<KnowledgeItemVO>> search(@RequestParam String keyword, KnowledgeSearchDTO dto){
        Long userId = UserContext.getUser();
        searchHistoryService.saveKeyword(keyword);
        return Result.ok(knowledgeItemService.getPage(dto));
    }

    @Operation(summary = "获取搜索历史")
    @GetMapping("/history")
    public Result<List<SearchHistoryVO>>searchHistory(@RequestParam (defaultValue = "10")int limit){
        Long userId = UserContext.getUser();
        return Result.ok(searchHistoryService.listRecent(limit));
    }


    @Operation(summary = "清空搜索历史")
    @DeleteMapping("/history")
    public Result<Void> clearHistory() {
        Long userId = UserContext.getUser();
        searchHistoryService.clearAll();
        return Result.ok();
    }


}
