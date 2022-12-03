package top.futurenotfound.log;

import top.futurenotfound.log.env.LogInfo;

public interface LogHandler {
    void handle(LogInfo logInfo);
}
