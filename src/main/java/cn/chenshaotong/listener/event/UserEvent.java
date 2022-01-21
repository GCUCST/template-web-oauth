package cn.chenshaotong.listener.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserEvent extends ApplicationEvent {

  private String msg;

  public UserEvent(Object source, String msg) {
    super(source);
    this.msg = msg;
  }
}
