package com.seabreeze.robot.plugin.okhttp;

import com.android.build.api.transform.Context;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformOutputProvider;
import com.seabreeze.robot.asm.transform.RobotTransform;
import com.seabreeze.robot.asm.transform.RunVariant;
import com.seabreeze.robot.plugin.okhttp.bytecode.OkHttpWeaver;

import org.gradle.api.Project;

import java.io.IOException;
import java.util.Collection;

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/9
 * @description : TODO
 * </pre>
 */
public class OkHttpRobotTransform extends RobotTransform {

    private Project project;
    private OkHttpRobotExtension okHttpHunterExtension;

    public OkHttpRobotTransform(Project project) {
        super(project);
        this.project = project;
        project.getExtensions().create("okHttpHunterExt", OkHttpRobotExtension.class);
        this.bytecodeWeaver = new OkHttpWeaver();
    }

    @Override
    public void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs,
                          TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        okHttpHunterExtension = (OkHttpRobotExtension) project.getExtensions().getByName("okHttpHunterExt");
        this.bytecodeWeaver.setExtension(okHttpHunterExtension);
        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental);
    }

    @Override
    protected RunVariant getRunVariant() {
        return okHttpHunterExtension.runVariant;
    }

    @Override
    protected boolean inDuplicatedClassSafeMode() {
        return okHttpHunterExtension.duplicatedClassSafeMode;
    }
}
