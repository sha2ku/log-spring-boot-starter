package top.futurenotfound.log;

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
public class ExecutorThreadHandler {
    private ThreadPoolExecutor logHandlePool;

    @PostConstruct
    public void init() {
        log.info("log-handle-pool init");
        logHandlePool = new ThreadPoolExecutor(
                200, 300,
                60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                new ThreadFactoryBuilder().setNameFormat("log-handle-pool-%d").build(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @PreDestroy
    public void destroy() {
        logHandlePool.shutdown();
        log.warn("log-handle-pool destroy");
    }
}
