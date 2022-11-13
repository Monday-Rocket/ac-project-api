package apply.ui.api

import org.springframework.http.HttpStatus

enum class ResponseCode(
    val statusCode: Int,
    val message: String,
    val httpStatus: HttpStatus
    ) {
    SUCCESS(0, "", HttpStatus.OK),

    NOT_AUTHORIZED(1000, "회원 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    NOT_SIGNED_UP(1001, "회원가입을 완료하지 않은 회원입니다.", HttpStatus.FORBIDDEN),

    HTTPS_MESSAGE_NOT_READABLE(9990, "메시지를 읽을 수 없습니다.", HttpStatus.BAD_REQUEST),
    METHOD_ARGUMENT_NOT_VALID(9991, "잘못된 메시지 형식입니다.", HttpStatus.BAD_REQUEST),
    ILLEGAL_ARGUMENT(9992, "유효하지 않은 데이터입니다.", HttpStatus.BAD_REQUEST)
}