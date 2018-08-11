package com.cx.item.common.par.check;

/**
 * 使用单例模式获取检测参数对象
 * Created by hwm on 2018/6/21.
 */
public class CheckParTargetInstance extends CheckParTarget {

    private static String lock = "lock";
    private static CheckParTarget checkParTarget;


    /**
     * 实例化检测参数的对象
     *
     * @param checkParInterface 检测对象
     * @param pars              需要检查的字段名称
     */
    private CheckParTargetInstance(CheckParInterface checkParInterface, String... pars) {
        super(checkParInterface, pars);
    }

    /**
     * 获取CheckParTarget对象
     *
     * @param checkParInterface
     * @param pars
     * @return
     */
    public static CheckParTarget getInstance(CheckParInterface checkParInterface, String... pars) {

        // 双重加锁判断
        if (checkParTarget == null) {
            synchronized (lock) {
                if (checkParTarget == null) {
                    checkParTarget = new CheckParTarget(checkParInterface, pars);
                }
            }
        }

        checkParTarget.setCheckParInterface(checkParInterface);
        checkParTarget.setPars(pars);
        return checkParTarget;
    }
}
