package top.futurenotfound.log.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "log")
@Data
public class LogProperties {
    private LogExecutorPoolProperties executorPool;

    public LogProperties(LogExecutorPoolProperties executorPool) {
        this.executorPool = executorPool;
    }
}
