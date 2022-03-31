package top.futurenotfound.log.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.futurenotfound.log.properties.LogExecutorPoolProperties;
import top.futurenotfound.log.properties.LogProperties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
@Getter
public class ThreadPoolExecutorHandler {
    private final LogProperties logProperties;
    private ThreadPoolExecutor logExecutorPool;

    public ThreadPoolExecutorHandler(LogProperties logProperties) {
        this.logProperties = logProperties;
    }

    @PostConstruct
    public void init() {
        log.info("log-executor-pool init");
        LogExecutorPoolProperties poolProperties = logProperties.getExecutorPool();
        logExecutorPool = new ThreadPoolExecutor(
                poolProperties.getCorePoolSize(), poolProperties.getMaximumPoolSize(),
                poolProperties.getKeepAliveTime(), poolProperties.getTimeUnit(),
                new LinkedBlockingDeque<>(poolProperties.getQueueCapacity()),
                new ThreadFactoryBuilder().setNameFormat("log-executor-pool-%d").build(),
                poolProperties.getRejectedPolicy().getHandler());
    }

    @PreDestroy
    public void destroy() {
        logExecutorPool.shutdown();
        log.warn("log-executor-pool destroy");
    }
}
