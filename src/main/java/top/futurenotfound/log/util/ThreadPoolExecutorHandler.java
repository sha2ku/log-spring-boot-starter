package top.futurenotfound.log.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.futurenotfound.log.properties.LogExecutorPoolProperties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
@Getter
public class ThreadPoolExecutorHandler {
    private final LogExecutorPoolProperties logExecutorPoolProperties;
    private ThreadPoolExecutor logExecutorPool;

    public ThreadPoolExecutorHandler(LogExecutorPoolProperties logExecutorPoolProperties) {
        this.logExecutorPoolProperties = logExecutorPoolProperties;
    }

    @PostConstruct
    public void init() {
        log.info("log-executor-pool init");
        logExecutorPool = new ThreadPoolExecutor(
                logExecutorPoolProperties.getCorePoolSize(),
                logExecutorPoolProperties.getMaximumPoolSize(),
                logExecutorPoolProperties.getKeepAliveTime(),
                logExecutorPoolProperties.getTimeUnit(),
                new LinkedBlockingDeque<>(logExecutorPoolProperties.getQueueCapacity()),
                new ThreadFactoryBuilder().setNameFormat("log-executor-pool-%d").build(),
                logExecutorPoolProperties.getRejectedPolicy().getHandler());
    }

    @PreDestroy
    public void destroy() {
        logExecutorPool.shutdown();
        log.warn("log-executor-pool destroy");
    }
}
