package com.pzx.knowledge.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeSyncMessage {
    private Long itemId;
    private String action;
    private Long timestamp;

}
