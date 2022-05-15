package com.example.common.response

import com.example.common.vo.basic.OperateError
import com.example.common.vo.basic.OperateResult

object ErrorResults {
    val NoCredentials = OperateResult(error = OperateError(title = "请提供账户名及密码", status = "NoCredentials"))

    val InvalidCredentials = OperateResult(error = OperateError(title = "账户名或密码不正确", status = "InvalidCredentials"))

    val SystemError = OperateResult(error = OperateError(title = "系统错误", status = "SystemError"))

    val Identityless = OperateResult(error = OperateError("未登录，请先登录。", "Identityless"))

    val InvalidArgument = OperateResult(error = OperateError("无效参数，请检查格式。", "InvalidArgument"))

    val RecordNotExist = OperateResult(error = OperateError("请求的记录不存在", "RecordNotExist"))
}