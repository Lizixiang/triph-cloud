package tripleh.triphcommon.com.response;

/**
 * Author: zixli
 * Date: 2020/8/20 13:47
 * FileName: ResultCode
 * Description: 状态码基类
 */
public interface ResultCode {

    /**
     * 操作是否成功， true:成功 false:失败
     */
    boolean isSuccess();

    /**
     * 状态码
     */
    int getCode();

    /**
     * 提示信息
     */
    String getMessage();


}
