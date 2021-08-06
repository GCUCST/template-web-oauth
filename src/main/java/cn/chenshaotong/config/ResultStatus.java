package cn.chenshaotong.config;

public enum ResultStatus {

  /** 操作成功 */
  OK(true, 200, "操作成功"),

  /** 操作失败 */
  FAIL(false, 500, "操作失败"),

  /** 请求失败 */
  BAD_REQUEST(false, 400, "请求失败"),

  /** 没有权限访问该资源 */
  UNAUTHORIZED(false, 401, "没有权限访问该资源"),

  /** 没有找到该资源 */
  NOT_FOUND(false, 404, "没有找到该资源"),

  /** 禁止访问 */
  FORBIDDEN(false, 403, "拒绝访问，您无权限查看此资源，请联系系统管理员处理"),

  /** 身份状态信息已过期 */
  TOKEN_EXPIRED(false, 422, "身份状态信息已过期，请重新登录");

  /** 操作是否成功 */
  private Boolean success;

  /** 状态码 */
  private Integer code;

  /** 消息提示 */
  private String message;

  ResultStatus(Boolean success, Integer code, String message) {
    this.success = success;
    this.code = code;
    this.message = message;
  }

  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
