package cc.sika.ai;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.junit.jupiter.api.Test;

/**
 * @author 小吴来哩
 * @since 2025-03
 */
class AtomicTest {
    volatile int i = 0;
    String text = """
                这是一段文本, 里面包含了中文还有English, 目的是为了testParticiple.
                是否能够准确的区分中English内容呢
                """;
    TokenizerEngine engine = TokenizerUtil.createEngine();
    Result parse = engine.parse(text);

    @Test
    void testAtomic() {
        AtomicDouble tokenLength = new AtomicDouble(0D);
        while (parse.hasNext()) {
            String wordContent = parse.next().getText();
            if (isChinese(wordContent)) {
                tokenLength.addAndGet(wordContent.length() * .6d);
            }
            else {
                tokenLength.addAndGet(wordContent.length() * .3d);
            }
        }
        System.out.println("tokenLength.get() = " + tokenLength.get());
        System.out.println("int tokenLength = " + (int)tokenLength.get());
    }

    private boolean isChinese(String word) {
        if (CharSequenceUtil.isBlank(word)) {
            return false;
        }
        char firstCharacter = word.charAt(0);
        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(firstCharacter);
        // 只包含汉字，排除标点
        return unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
                unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
                unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B ||
                unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C ||
                unicodeBlock == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D ||
                unicodeBlock == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS;
    }
}
