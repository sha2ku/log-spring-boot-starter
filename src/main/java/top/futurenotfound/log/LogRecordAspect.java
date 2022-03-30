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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

@Aspect
@Slf4j
@Component
@AllArgsConstructor
public class LogRecordAspect {

    private final LogHandler logHandler;
    private final SpElHandler spElHandler;
    private final ApplicationContext applicationContext;
    private final ExecutorThreadHandler executorThreadHandler;

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
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        //spel bean expression
        evaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
        //spel map expression
        evaluationContext.addPropertyAccessor(new MapAccessor());

        try {
            int size = Objects.requireNonNull(params).length;
            Map<String, Object> variables = new HashMap<>(size);
            for (int index = 0; index < size; index++) {
                variables.put(params[index], args[index]);
            }
            //spel object/parameter expression
            evaluationContext.setVariables(variables);
        } catch (Exception e) {
            log.error("cannot resolver request params' name.");
        }

        String contentExpression = logRecord.contentExpression();
        String operatorExpression = logRecord.operatorExpression();
        String timestampExpression = logRecord.timestampExpression();

        String content = spElHandler.replaceMultipleParameterExpression(evaluationContext, contentExpression);
        String operator = spElHandler.replaceExpression(evaluationContext, operatorExpression, String.class);
        String timestamp = spElHandler.replaceExpression(evaluationContext, timestampExpression, String.class);

        LogInfo currentLog = new LogInfo();
        currentLog.setOperator(operator);
        currentLog.setLogContent(content);
        currentLog.setTimestamp(timestamp);

        ThreadPoolExecutor threadPoolExecutor = executorThreadHandler.getLogHandlePool();
        threadPoolExecutor.execute(() -> logHandler.handle(currentLog));

        return joinPoint.proceed();
    }
}