package com.pzx.knowledge.controller;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.pzx.knowledge.common.result.Result;
import com.pzx.knowledge.common.result.ResultCode;
import com.pzx.knowledge.dto.KnowledgeItemDTO;
import com.pzx.knowledge.dto.KnowledgeSearchDTO;
import com.pzx.knowledge.service.KnowledgeItemService;
import com.pzx.knowledge.vo.KnowledgeItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;


@Tag(name="CRUD 接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/items")
public class KnowledgeItemController {
    private final KnowledgeItemService knowledgeItemservice;


    @SentinelResource(value = "knowledge_create",blockHandler = "createBlock")
    @Operation(summary = "新增知识库笔记")
    @PostMapping
    public Result<KnowledgeItemVO>create(@Valid @RequestBody KnowledgeItemDTO dto){
        return Result.ok(knowledgeItemservice.create(dto));
    }
    public Result<KnowledgeItemVO> createBlock(KnowledgeItemDTO dto, BlockException ex){
        return Result.fail(ResultCode.SERVER_BUSY);
    }



    @Operation(summary = "修改笔记")
    @PutMapping("/{id}")
    public  Result<KnowledgeItemVO> update (@PathVariable Long id ,@Valid @RequestBody KnowledgeItemDTO dto){
        return Result.ok(knowledgeItemservice.update(id,dto));
    }



    @DeleteMapping("/{id}")
    @Operation(summary = "删除笔记")
    public Result <Void> delete (@PathVariable Long id){
        knowledgeItemservice.delete(id);
        return Result.ok();
    }



    @Operation(summary = "分页查询知识库")
    @GetMapping
    public Result<Page<KnowledgeItemVO>>list(@Valid KnowledgeSearchDTO dto){
        return Result.ok(knowledgeItemservice.getPage(dto));
    }



    @Operation(summary = "查看笔记")
    @GetMapping("/{id}")
    public Result <KnowledgeItemVO> detail(@PathVariable Long id){
        return Result.ok(knowledgeItemservice.getDetail(id));
    }




    @Operation(summary = "置顶或取消")
    @PutMapping("/{id}/top")
    public Result<Void> toggleTop(@PathVariable Long id ,@RequestParam boolean isTop)
    {
        knowledgeItemservice.toggleTop(id,isTop);
        return Result.ok();
    }

}
