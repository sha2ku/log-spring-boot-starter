package top.futurenotfound.log;

import lombok.extern.slf4j.Slf4j;
import top.futurenotfound.log.env.LogInfo;

@Slf4j
public class DefaultLogHandler implements LogHandler {
    @Override
    public void handle(LogInfo logInfo) {
        log.info(String.format("%s |%s| operator:%s, %s", logInfo.getTimestamp(), logInfo.getModelName(), logInfo.getOperator(), logInfo.getLogContent()));
    }
}
