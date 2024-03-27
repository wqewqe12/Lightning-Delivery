package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Component//实例化，交给ioc容器管理
public class MyTask {
    /**
     * 定时任务，5s一次
     */
    //@Scheduled(cron = "0/5 * * * * ?")
    public void excuteTask(){
        log.info("定时任务开启执行:{}",new Date());
    }
}
