package top.futurenotfound.log.handler;

import lombok.extern.slf4j.Slf4j;
import top.futurenotfound.log.LogInfo;

@Slf4j
public class DefaultLogHandler implements LogHandler {
    @Override
    public void handle(LogInfo logInfo) {
        log.info(String.format("%s | operator:%s, %s", logInfo.getTimestamp(), logInfo.getOperator(), logInfo.getLogContent()));
    }
}
