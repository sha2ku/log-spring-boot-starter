package top.futurenotfound.log;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.futurenotfound.log.handler.DefaultLogHandler;
import top.futurenotfound.log.handler.LogHandler;

@Configuration
public class LogAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(LogHandler.class)
    public LogHandler logHandler() {
        return new DefaultLogHandler();
    }
}
