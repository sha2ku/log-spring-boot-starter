package top.futurenotfound.log.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.futurenotfound.log.DefaultLogHandler;
import top.futurenotfound.log.LogHandler;
import top.futurenotfound.log.env.LogExecutorPoolProperties;

@Configuration
@EnableConfigurationProperties({LogExecutorPoolProperties.class})
public class LogAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(LogHandler.class)
    public LogHandler logHandler() {
        return new DefaultLogHandler();
    }
}
