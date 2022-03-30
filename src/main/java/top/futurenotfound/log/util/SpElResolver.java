package top.futurenotfound.log.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@AllArgsConstructor
public class SpElResolver {
    private static final ExpressionParser parser = new SpelExpressionParser();

    /**
     * just support spel parameter expression.
     * <p>
     * e.g {@code "sple test: #user.name do something in #datetime"}
     */
    public String replaceMultipleParameterExpression(EvaluationContext evaluationContext, String content, String nullFillWord) {
        Pattern pattern = Pattern.compile("#[\\w\\\\.]+");
        Matcher matcher = pattern.matcher(content);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String expression = matcher.group();
            try {
                String value = replaceExpression(evaluationContext, expression, String.class);
                if (value == null) value = nullFillWord;
                matcher.appendReplacement(buffer, value);
            } catch (Exception ignored) {
                //do nothing
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public <T> T replaceExpression(EvaluationContext evaluationContext, String expression, Class<T> clazz) {
        return parser.parseExpression(expression).getValue(evaluationContext, clazz);
    }
}
