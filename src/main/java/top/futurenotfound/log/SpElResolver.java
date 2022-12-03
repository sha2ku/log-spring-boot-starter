package top.futurenotfound.log;

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
     * multiple expression must be enclosed in backquotes({@code `})
     */
    public String multipleExpression(EvaluationContext evaluationContext, String content, String nullFillWord) {
        Pattern pattern = Pattern.compile("`(.*?)`");
        Matcher matcher = pattern.matcher(content);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String expression = matcher.group().replace("`", "");
            try {
                String value = singleExpression(evaluationContext, expression, String.class);
                if (value == null) value = nullFillWord;
                matcher.appendReplacement(buffer, value);
            } catch (Exception ignored) {
                //do nothing
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public <T> T singleExpression(EvaluationContext evaluationContext, String expression, Class<T> clazz) {
        return parser.parseExpression(expression).getValue(evaluationContext, clazz);
    }
}
