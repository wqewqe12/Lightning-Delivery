package com.sky.task;

import com.sky.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WebSocketTask {
    @Autowired
    private WebSocketService webSocketService;
    /**
     * 通过websocket 5秒发送一次请求
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void sendMessageToClient(){
        webSocketService.sendToAllClient("这是来自服务端的消息:"+ DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
    }
}
