package cc.sika.ai.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
@SpringBootTest
public class HanLpTest {

    @Test
    void testTokenizerUtil() {
        TokenizerEngine engine = TokenizerUtil.createEngine();
        String text = """
                这是一段文本, 里面包含了中文还有English, 目的是为了testParticiple.
                是否能够准确的区分中English内容呢
                """;
        Result result = engine.parse(text);
        String resultStr = CollUtil.join((Iterator<Word>)result, " ");
        System.out.println(resultStr);
    }
}
