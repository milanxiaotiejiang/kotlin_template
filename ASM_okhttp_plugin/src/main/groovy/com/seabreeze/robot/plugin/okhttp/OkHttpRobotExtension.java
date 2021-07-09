package com.seabreeze.robot.plugin.okhttp;

import com.seabreeze.robot.asm.transform.RunVariant;

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/9
 * @description : TODO
 * </pre>
 */
public class OkHttpRobotExtension {

    public RunVariant runVariant = RunVariant.ALWAYS;
    public boolean weaveEventListener = true;
    public boolean duplicatedClassSafeMode = false;

    @Override
    public String toString() {
        return "OkHttpHunterExtension{" +
                "runVariant=" + runVariant +
                ", weaveEventListener=" + weaveEventListener +
                ", duplicatedClassSafeMode=" + duplicatedClassSafeMode +
                '}';
    }
}
