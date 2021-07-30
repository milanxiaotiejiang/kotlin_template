package com.seabreeze.robot.third.model

import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.Either
import com.seabreeze.robot.base.model.BaseEvent
import com.seabreeze.robot.third.ext.WxResult
import com.tencent.mm.opensdk.modelbase.BaseResp

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/17
 * @description : TODO
 * </pre>
 */
class WxLoginEvent(either: Either<WxResult, BaseThrowable>) :
    BaseEvent<Either<WxResult, BaseThrowable>>(either)

class WxPayEvent(resp: BaseResp) : BaseEvent<BaseResp>(resp)
class WxShareEvent(resp: BaseResp) : BaseEvent<BaseResp>(resp)