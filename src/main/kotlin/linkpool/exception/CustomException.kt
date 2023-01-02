package linkpool.exception

import linkpool.ui.api.ResponseCode

class CustomException(
    val responseCode: ResponseCode,
    val data: Any? = null
): RuntimeException()