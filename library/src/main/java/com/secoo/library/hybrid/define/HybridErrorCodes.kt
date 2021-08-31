package com.secoo.library.hybrid.define

const val ERROR_CODE_OK = 0
const val ERROR_CODE_UNAVAILABLE = 1
const val ERROR_CODE_ILLEGAL_ARGUMENT = 4
const val ERROR_CODE_NO_NETWORK = 5

/**
 * 状态异常，比如用户主动取消或者流程被停止
 */
const val ERROR_CODE_ILLEGAL_STATE = ERROR_CODE_NO_NETWORK + 1