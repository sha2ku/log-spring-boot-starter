package top.futurenotfound.log;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogRecord {
    String contentExpression();

    String operatorExpression() default "T(top.futurenotfound.log.CurrentUser).get()";

    String timestampExpression() default "T(java.lang.System).currentTimeMillis().toString()";
}
