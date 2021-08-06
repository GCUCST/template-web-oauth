package cn.chenshaotong.listener;


import cn.chenshaotong.listener.event.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomEventListener {

    @EventListener
    public void listenExpiredOrder(UserEvent event) {
        System.out.println("我是监听器，接收到："+event.getMsg());
    }



}
