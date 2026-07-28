package com.pzx.knowledge;

import com.pzx.knowledge.common.result.Result;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KnowledgeSystemApplicationTests {

    @Test
    void testResult() {
        Result<String> r = Result.ok("hello");
        System.out.println(r);
    }
}
