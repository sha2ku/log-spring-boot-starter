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

1. `multipleExpression` 为多表达式解析方法，传入的语句中的 SpEL **需使用反引号 `` ` `` 括起来**，例：`

```java  
@LogRecord(
    contentExpression = "金额变更: 金额从 `@testServiceImpl.getAmountByOrderId(#orderInfo.orderId)` 元变更为 `#orderInfo.newAmount` 元"
)
```

2. `singleExpression` 为单表达式解析方法，SpEL 语句**无需使用反引号 `` ` `` 括起来**，例：

```java  
@LogRecord(
    operatorExpression = "@testServiceImpl.getUsernameById(#orderInfo.id)"
)
```

## How to use

see [log-demo](https://github.com/ming-lz/log-demo)