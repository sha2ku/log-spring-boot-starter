package top.futurenotfound.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.futurenotfound.log.env.ThreadPoolExecutorRejectedPolicy;

import java.util.concurrent.TimeUnit;

@Data
@ConfigurationProperties(prefix = "log.executor-pool")
public class LogExecutorPoolProperties {
    private int corePoolSize = 100;
    private int maximumPoolSize = 200;
    private long keepAliveTime = 60L;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private int queueCapacity = 100000;

    private ThreadPoolExecutorRejectedPolicy rejectedPolicy = ThreadPoolExecutorRejectedPolicy.ABORT;
}
