package top.futurenotfound.log;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@AllArgsConstructor
public class SpElHandler {
    private static final ExpressionParser parser = new SpelExpressionParser();

    /**
     * just support spel parameter expression.
     * <p>
     * e.g {@code "sple test: #user.name do something in #datetime"}
     */
    public String replaceMultipleParameterExpression(EvaluationContext evaluationContext, String content) {
        Pattern pattern = Pattern.compile("#[\\w\\\\.]+");
        Matcher matcher = pattern.matcher(content);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String expression = matcher.group();
            String value = replaceBeanExpression(evaluationContext, expression, String.class);
            if (value != null) matcher.appendReplacement(buffer, value);
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public <T> T replaceExpression(EvaluationContext evaluationContext, String expression, Class<T> clazz) {
        try {
            return parser.parseExpression(expression).getValue(evaluationContext, clazz);
        } catch (EvaluationException | ParseException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public <T> T replaceBeanExpression(EvaluationContext evaluationContext, String expression, Class<T> clazz) {
        return replaceExpression(evaluationContext, expression, clazz);
    }
}
