package top.futurenotfound.log;

import top.futurenotfound.log.domain.LogInfo;

public interface LogHandler {
    void handle(LogInfo logInfo);
}
