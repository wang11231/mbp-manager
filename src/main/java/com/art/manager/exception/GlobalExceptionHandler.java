package com.art.manager.exception;

import com.art.manager.pojo.Msg;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Msg dexcepitonHandler(HttpServletRequest request, Exception e) {
        log.error(e.getMessage(), e);
        if (e instanceof ExcelException) {
            return new Msg(Msg.FAILURE_CODE, "读取excel出错，请检查excel格式或者excel内数据格式！", new Date());
        }
        else if ((e instanceof PointLoginException) || (e instanceof ParameterException) || (e instanceof IllegalArgumentException)) {
            return new Msg(Msg.FAILURE_CODE, e.getMessage(), new Date());
        }

        return new Msg(Msg.FAILURE_CODE, "服务器异常，请联系管理员！", new Date());
    }

}
