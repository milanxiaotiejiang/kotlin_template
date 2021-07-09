package com.seabreeze.robot.asm.transform;

import java.io.IOException;
import java.io.InputStream;

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/9
 * @description : TODO
 * </pre>
 */
public interface IWeaver {
    boolean isWeavableClass(String filePath) throws IOException;

    byte[] weaveSingleClassToByteArray(InputStream inputStream) throws IOException;
}
