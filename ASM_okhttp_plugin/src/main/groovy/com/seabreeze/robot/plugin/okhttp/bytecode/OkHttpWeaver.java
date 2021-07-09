package com.seabreeze.robot.plugin.okhttp.bytecode;

import com.seabreeze.robot.asm.transform.BaseWeaver;
import com.seabreeze.robot.plugin.okhttp.OkHttpRobotExtension;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/9
 * @description : TODO
 * </pre>
 */
public final class OkHttpWeaver extends BaseWeaver {

    private OkHttpRobotExtension okHttpRobotExtension;

    @Override
    public void setExtension(Object extension) {
        if (extension == null) return;
        this.okHttpRobotExtension = (OkHttpRobotExtension) extension;
    }

    @Override
    protected ClassVisitor wrapClassWriter(ClassWriter classWriter) {
        return new OkHttpClassAdapter(classWriter, this.okHttpRobotExtension.weaveEventListener);
    }

}
