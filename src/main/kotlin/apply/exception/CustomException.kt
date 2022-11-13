package apply.exception

import apply.ui.api.ResponseCode

class CustomException(
    val responseCode: ResponseCode,
    val data: Any? = null
): Exception()