package tripleh.triphcommon.com.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tripleh.triphcommon.com.response.CommonResultCode;
import tripleh.triphcommon.com.response.ResponseResult;

/**
 * Author: zixli
 * Date: 2020/8/20 15:39
 * FileName: CommonExceptionHandler
 * Description: 增强控制器-全局异常
 */
@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    /**
     * 系统异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exceptionHandler(Exception e) {
        return ResponseResult.FAIL(CommonResultCode.SERVER_ERROR.getMessage());
    }


    /**
     * 业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(ServiceException e) {
        return ResponseResult.FAIL(CommonResultCode.BUSINESS_ABNORMAL.getCode(), e.getLocalizedMessage());
    }

}
