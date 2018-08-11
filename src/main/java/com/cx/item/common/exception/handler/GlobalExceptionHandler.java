package com.cx.item.common.exception.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.extra.servlet.ServletUtil;
import com.cx.item.common.exception.CommonException;
import com.cx.item.common.vo.ResultPageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 异常处理类
 * Created by h on 2018/5/6.
 */
public abstract class GlobalExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = CommonException.class)
    public ResultPageVo<Void> commonExceptionHandler(HttpServletRequest req, CommonException e) throws Exception {

        outputErr(req, e, 1);
        return ResultPageVo.failure(e.getMsg());
    }

    @ExceptionHandler(value = Exception.class)
    public ResultPageVo<Void> defaultExceptionHandler(HttpServletRequest req, Exception e) throws Exception {

        outputErr(req, e, 2);
        return ResultPageVo.failure("操作失败");
    }

    /**
     * 打印异常信息
     *
     * @param req
     * @param e
     * @param excType 1业务异常，2系统异常
     */
    private void outputErr(HttpServletRequest req, Exception e, int excType) {

        String title = excType == 1 ? "业务" : "系统";
        Map<String, String> paramMap = ServletUtil.getParamMap(req);

        outprint(excType, StrFormatter.format("\r\n\r\n====>{}异常，进入异常拦截方法中...", title));

        if (MapUtil.isNotEmpty(paramMap)) {
            outprint(excType, (StrFormatter.format("\r\n====param!!!!请求参数：{}", MapUtil.join(paramMap, "&", "="))));
        } else {
            outprint(excType, "\r\n====没有请求参数（或者请求参数都为空）!!!!");
        }
        outprint(excType, "\r\n====errorMsg!!!!具体异常信息", e);

        outprint(excType, StrFormatter.format("\r\n<===={}异常，进入异常拦截方法结束！！！\r\n\r\n", title));
    }

    /**
     * 1info类型输出，2错误日志类型输出
     *
     * @param outExcType
     * @param errStr
     * @param e
     */
    private void outprint(int outExcType, String errStr, Exception... e) {

        Exception ex = null;
        if (e.length > 0) {
            ex = e[0];
        }

        if (outExcType == 1) {
            if (ex == null) {
                log.info(errStr);
            } else {
                log.info(errStr, ex);
            }
        } else {
            if (ex == null) {
                log.error(errStr);
            } else {
                log.error(errStr, ex);
            }
        }

    }
}