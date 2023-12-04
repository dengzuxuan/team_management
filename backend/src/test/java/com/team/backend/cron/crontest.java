package com.team.backend.cron;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  TODO
 * </p>
 *
 * @author Colin
 * @since 2023/12/4
 */
@Configuration
@EnableScheduling
public class crontest {
    @Scheduled(fixedRate=5000)
    public void sayWord() {
        System.out.println("world");
    }
}
