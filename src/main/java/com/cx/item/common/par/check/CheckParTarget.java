package com.cx.item.common.par.check;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.cx.item.common.constants.ResultCode;
import com.cx.item.common.exception.ParamsCheckException;
import com.cx.item.common.vo.ResultPageVo;

import java.lang.reflect.Method;

/**
 * 检查的具体实现
 * 推荐使用CheckParTargetInstance类获取实现
 * Created by hwm on 2018/5/30.
 */
public class CheckParTarget {

    private CheckParInterface checkParInterface;
    private String[] pars;
    private boolean isCheckAll = true; // true检测所有参数后返回,false遇到报错就返回

    /**
     * 实例化检测参数的对象
     * 推荐使用CheckParTargetInstance类获取实现
     *
     * @param checkParInterface 检测对象
     * @param pars              需要检查的字段名称
     */
    @Deprecated
    public CheckParTarget(CheckParInterface checkParInterface, String... pars) {
        this.checkParInterface = checkParInterface;
        this.pars = pars;
    }

    /**
     * true检测所有参数后返回,false遇到报错就返回
     *
     * @param isCheckAll
     */
    public void setIsCheckAll(boolean isCheckAll) {
        this.isCheckAll = isCheckAll;
    }

    protected void setPars(String[] pars) {
        this.pars = pars;
    }

    protected void setCheckParInterface(CheckParInterface checkParInterface) {
        this.checkParInterface = checkParInterface;
    }

    /**
     * 验证参数方法是否为空
     *
     * @return
     */
    public ResultPageVo checkParIsNull() {

        return checkPar();
    }

    /**
     * 验证参数方法是否为空,参数为空抛出异常信息
     *
     * @return
     */
    public ResultPageVo checkParIsNullThrowError() {

        ResultPageVo resultPageVo = checkPar();
        if (!ResultCode.SUCCESS.status().equals(resultPageVo.getStatus())) {
            throw new ParamsCheckException(resultPageVo.getStatus(), resultPageVo.getMessage());
        }

        return resultPageVo;
    }

    private ResultPageVo checkPar() {

        if (ArrayUtil.isEmpty(pars)) {
            return ResultPageVo.success();
        }

        Class<? extends CheckParInterface> aClass = checkParInterface.getClass();
        // 检查空值
        StringBuilder checkParMsg = new StringBuilder();
        for (String str : pars) {

            try {
                String getMethodName = "get"
                        + str.substring(0, 1).toUpperCase()
                        + str.substring(1);

                Method getMethod = aClass.getMethod(getMethodName, new Class[]{});
                Object value = getMethod.invoke(this.checkParInterface, new Object[]{});

                if (ObjectUtil.isNull(value)) {
                    if (isCheckAll) {
                        checkParMsg.append(str).append(",");
                    } else {
                        return ResultPageVo.failure(StrUtil.format("字段{}为空", str));
                    }
                }
            } catch (Exception e) {
            }
        }

        if (StrUtil.isNotEmpty(checkParMsg)) {
            return ResultPageVo.failure(StrUtil.format("字段【{}】为空", StrUtil.sub(checkParMsg, 0, -1)));
        }

        return ResultPageVo.success();
    }

}
