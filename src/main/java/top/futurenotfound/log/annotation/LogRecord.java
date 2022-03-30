package top.futurenotfound.log.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogRecord {
    String contentExpression();

    String operatorExpression() default "T(top.futurenotfound.log.util.CurrentUser).get()";

    String timestampExpression() default "T(java.lang.System).currentTimeMillis().toString()";
}
