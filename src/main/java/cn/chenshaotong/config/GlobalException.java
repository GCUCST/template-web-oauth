package cn.chenshaotong.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 全局异常 */
@RestControllerAdvice
public class GlobalException {

  /** Exception */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResModel<String> handleException(Exception e) {
    System.err.println("处理异常：" + e.getMessage());
    return ResModel.create(ResultStatus.FAIL, e.getMessage());
  }

  @ExceptionHandler(ArrayStoreException.class)
  @ResponseStatus(HttpStatus.SEE_OTHER)
  public ResModel<String> handleNotFoundException(Exception e) {
    System.err.println("处理异常...：" + e.getMessage());
    return ResModel.create(ResultStatus.FAIL, e.getMessage());
  }
}
