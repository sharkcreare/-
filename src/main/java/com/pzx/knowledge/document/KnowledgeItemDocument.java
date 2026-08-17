package com.pzx.knowledge.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/** ES 索引文档（对应 knowledge_item 索引） */
@Data
@Document(indexName = "knowledge_item")
public class KnowledgeItemDocument {

    @Id
    private Long id;

    private Long userId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Text)
    private String summary;

    @Field(type = FieldType.Keyword)
    private String contentType;

    @Field(type = FieldType.Keyword)
    private String sourceUrl;

    private Integer isFavorite;
    private Integer isTop;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}