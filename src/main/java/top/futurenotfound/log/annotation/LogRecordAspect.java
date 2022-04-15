package top.futurenotfound.log.annotation;

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
import top.futurenotfound.log.LogHandler;
import top.futurenotfound.log.LogInfo;
import top.futurenotfound.log.util.SpElResolver;
import top.futurenotfound.log.util.ThreadPoolExecutorHandler;

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
    private final SpElResolver spElResolver;
    private final ApplicationContext applicationContext;
    private final ThreadPoolExecutorHandler threadPoolExecutorHandler;

    @Pointcut("@annotation(top.futurenotfound.log.annotation.LogRecord)")
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
        String nullFillWord = logRecord.nullFillWord();
        String modelName = logRecord.modelName();

        String content = spElResolver.multipleExpression(evaluationContext, contentExpression, nullFillWord);
        String operator = spElResolver.singleExpression(evaluationContext, operatorExpression, String.class);
        String timestamp = spElResolver.singleExpression(evaluationContext, timestampExpression, String.class);

        LogInfo currentLogInfo = new LogInfo(operator, timestamp, content, modelName);

        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorHandler.getLogExecutorPool();
        threadPoolExecutor.execute(() -> logHandler.handle(currentLogInfo));

        return joinPoint.proceed();
    }
}