package cn.chenshaotong.config;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ResModel<T> implements Serializable {

  /** 操作是否成功 */
  private Boolean success;

  /** 状态码 */
  private Integer code;

  /** 消息提示 */
  private String message;

  /** 数据 */
  private T data;

  public ResModel() {}

  public ResModel(Boolean success, Integer code, String message) {
    this.success = success;
    this.code = code;
    this.message = message;
  }

  public ResModel(Boolean success, Integer code, String message, T data) {
    this.success = success;
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static <T> ResModel<T> ok() {
    return create(ResultStatus.OK, ResultStatus.OK.getMessage());
  }

  public static <T> ResModel<T> ok(T data) {
    return create(ResultStatus.OK, ResultStatus.OK.getMessage(), data);
  }

  public static <T> ResModel<T> okForLayui(T data) {
    return new ResModel<>(true, 0, "success", data);
  }

  public static <T> ResModel<T> fail(ResultStatus code) {
    return create(code, code.getMessage());
  }

  public static <T> ResModel<T> fail(String errorMsg) {
    return create(ResultStatus.FAIL, errorMsg);
  }

  public static <T> ResModel<T> notFound(String errorMsg) {
    return create(ResultStatus.NOT_FOUND, errorMsg);
  }

  public static <T> ResModel<T> create(ResultStatus code, String message) {
    return new ResModel<>(code.getSuccess(), code.getCode(), message);
  }

  public static <T> ResModel<T> create(ResultStatus code, String message, T data) {
    return new ResModel<>(code.getSuccess(), code.getCode(), message, data);
  }
}
