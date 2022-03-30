package top.futurenotfound.log.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@Getter
public class ThreadPoolExecutorHandler {
    private ThreadPoolExecutor logExecutorPool;

    @PostConstruct
    public void init() {
        log.info("log-executor-pool init");
        logExecutorPool = new ThreadPoolExecutor(
                100, 200,
                60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                new ThreadFactoryBuilder().setNameFormat("log-executor-pool-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @PreDestroy
    public void destroy() {
        logExecutorPool.shutdown();
        log.warn("log-executor-pool destroy");
    }
}
