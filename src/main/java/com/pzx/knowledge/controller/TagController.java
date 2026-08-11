package com.pzx.knowledge.controller;

import com.pzx.knowledge.annotation.OperationLog;
import com.pzx.knowledge.common.result.Result;
import com.pzx.knowledge.dto.TagDTO;
import com.pzx.knowledge.service.TagService;
import com.pzx.knowledge.utils.UserContext;
import com.pzx.knowledge.vo.TagVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "标签管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;


    @Operation(summary = "获取标签列表")
    @GetMapping
    public Result<List<TagVO>> list() {
        return Result.ok(tagService.listByUser(UserContext.getUser()));
    }


    @OperationLog(module = "标签",type = "新增",desc = "新增标签")
    @PostMapping
    @Operation(summary = "创建标签")
    public Result<TagVO> create(@Valid @RequestBody TagDTO dto){
        return Result.ok(tagService.create(UserContext.getUser(), dto));
    }




    @OperationLog(module = "标签",type = "修改",desc = "修改标签")
    @PutMapping("/{id}")
    public Result<TagVO> update(@PathVariable Long id ,@Valid @RequestBody TagDTO dto){
        return Result.ok(tagService.update(id,UserContext.getUser(), dto));
    }



    @OperationLog(module = "标签",type ="删除" ,desc = "删除标签")
    @DeleteMapping("/{id}")
    public Result<TagVO> delete(@PathVariable Long id){
        tagService.delete(id,UserContext.getUser());
        return Result.ok();
    }
}
