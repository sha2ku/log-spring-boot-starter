package top.futurenotfound.log.env;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Getter
@AllArgsConstructor
public enum ThreadPoolExecutorRejectedPolicy {
    //丢弃当前任务并报异常
    ABORT(new ThreadPoolExecutor.AbortPolicy()),
    //直接丢弃当前任务
    DISCARD(new ThreadPoolExecutor.DiscardPolicy()),
    //直接丢弃最早入队的任务
    DISCARD_OLDEST(new ThreadPoolExecutor.DiscardOldestPolicy()),
    //无视队列直接执行当前任务
    CALLER_RUNS(new ThreadPoolExecutor.CallerRunsPolicy()),

    ;

    private final RejectedExecutionHandler handler;
}
