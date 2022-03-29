package top.futurenotfound.log;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import top.futurenotfound.log.handler.LogHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Slf4j
@Component
@AllArgsConstructor
public class LogRecordAspect {

    private final LogHandler logHandler;
    private final SpElHandler spElHandler;
    private final ApplicationContext applicationContext;

    @Pointcut("@annotation(top.futurenotfound.log.LogRecord)")
    public void pointcut() {
        //nothing to do
    }

    @Around("pointcut()")
    public Object joinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> clazz = joinPoint.getTarget().getClass();
        Method method = clazz.getDeclaredMethod(signature.getName(), signature.getMethod().getParameterTypes());

        LogRecord logRecord = method.getAnnotation(LogRecord.class);

        Object[] args = joinPoint.getArgs();
        String[] params = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
        StandardEvaluationContext context = new StandardEvaluationContext();
        //spel bean expression
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        //spel map expression
        context.addPropertyAccessor(new MapAccessor());

        try {
            int size = Objects.requireNonNull(params).length;
            Map<String, Object> variables = new HashMap<>(size);
            for (int index = 0; index < size; index++) {
                variables.put(params[index], args[index]);
            }
            //spel object/parameter expression
            context.setVariables(variables);
        } catch (Exception e) {
            log.error("cannot resolver request params' name.");
        }

        String contentExpression = logRecord.contentExpression();
        String operatorExpression = logRecord.operatorExpression();

        String logContent = spElHandler.replaceMultipleParameterExpression(context, contentExpression);
        String operator = spElHandler.replaceExpression(context, operatorExpression, String.class);

        LogInfo currentLog = new LogInfo();
        currentLog.setOperator(operator);
        currentLog.setLogContent(logContent);
        currentLog.setTimestamp(System.currentTimeMillis());

        logHandler.handle(currentLog);

        return joinPoint.proceed();
    }
}