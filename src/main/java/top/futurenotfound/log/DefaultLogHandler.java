package top.futurenotfound.log;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultLogHandler implements LogHandler {
    @Override
    public void handle(LogInfo logInfo) {
        log.info(String.format("%s | operator:%s, %s", logInfo.getTimestamp(), logInfo.getOperator(), logInfo.getLogContent()));
    }
}
