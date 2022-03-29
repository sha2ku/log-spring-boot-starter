# log-spring-boot-starter

用来用注解 + spEL 表达式来记录日志的 starter。

## 自定义 LogHandler（日志处理器）

默认的 `LogHandler` 为 sl4j 打印日志到日志文件和控制台，如果需要自定义 `LogHandler`，则直接在自己项目中创建类似：

```java
import org.springframework.stereotype.Component;

@Component
public class MQLogHandler implements LogHandler {
    @Override
    public void handle(LogInfo logInfo) {
        //mq producer

    }
}
```

## 自定义 operator（操作人）

默认的 operator 为从 `CurrentUser` 中获取，则需要在方法执行之前，比如在 `Filter` 里面处理 jwt 之后用 set 方法赋值。 如果有更复杂的逻辑，则可以自定义 operatorExpression
来实现类似传入参数从数据库查询 operator 这样的操作。

## 其他

1. `replaceMultipleParameterExpression` 只支持 `#user.name` 或者 `#name` 的形式。
2. `replaceExpression` 除了支持以上形式外还支持静态方法表达式和 bean 方法表达式。（bean 方法表达式需注意 bean 名称应为实现类 bean 名称。例如：testServiceImpl 而不是
   testService）