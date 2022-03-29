package top.futurenotfound.log;

import lombok.Data;

@Data
public class LogInfo {
    private String operator;
    private long timestamp;
    private String logContent;
}
