package com.pzx.knowledge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzx.knowledge.common.result.Result;
import com.pzx.knowledge.service.FavoriteService;
import com.pzx.knowledge.utils.UserContext;
import com.pzx.knowledge.vo.KnowledgeItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "收藏管理")
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "添加收藏")
    @PostMapping("/{itemId}")
    public Result<Void> add(@PathVariable Long itemId) {
        favoriteService.add(itemId);
        return Result.ok();
    }

    @Operation(summary = "取消收藏")
    @DeleteMapping("/{itemId}")
    public Result<Void> remove(@PathVariable Long itemId) {
        favoriteService.remove(itemId);
        return Result.ok();
    }

    @Operation(summary = "获取收藏列表")
    @GetMapping
    public Result<Page<KnowledgeItemVO>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.ok(favoriteService.pageList(pageNum, pageSize));
    }
}