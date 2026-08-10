package com.pzx.knowledge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 标签请求 DTO —— 独立成类，Controller/Service 共用，可被校验注解约束
 *
 * 为什么用 DTO 不用内部类：
 * - 内部类无法被其他层复用，测试也不方便
 * - 校验注解（@NotBlank/@Size/@Pattern）集中定义在这里，接口层只管声明 @Valid
 */
@Data
public class TagDTO {

    @NotBlank(message = "标签名不能为空")
    @Size(max = 20, message = "标签名最长20个字符")
    private String name;

    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "颜色格式必须是 #RRGGBB，例如 #1677ff")
    private String color;
}