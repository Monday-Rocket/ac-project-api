package linkpool.ui.api

import linkpool.domain.user.UnidentifiedUserException
import linkpool.exception.CustomException
import linkpool.security.LoginFailedException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import support.infra.ExceededRequestException
import javax.persistence.EntityNotFoundException

@RestControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("message", ex)
        return ResponseEntity.status(ResponseCode.HTTPS_MESSAGE_NOT_READABLE.httpStatus)
            .body(ApiResponse.error(ResponseCode.HTTPS_MESSAGE_NOT_READABLE))
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("message", ex)
        return ResponseEntity.status(ResponseCode.METHOD_ARGUMENT_NOT_VALID.httpStatus)
            .body(ApiResponse.error(ResponseCode.METHOD_ARGUMENT_NOT_VALID))
    }

    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleBadRequestException(exception: RuntimeException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(ResponseCode.ILLEGAL_ARGUMENT.httpStatus)
            .body(
                ApiResponse.error(
                    status = ResponseCode.ILLEGAL_ARGUMENT.statusCode,
                    message = exception.message ?: ResponseCode.ILLEGAL_ARGUMENT.message
                ))
    }

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(exception: CustomException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(exception.responseCode.httpStatus)
            .body(ApiResponse.error(exception.responseCode))
    }

    @ExceptionHandler(LoginFailedException::class)
    fun handleUnauthorizedException(exception: LoginFailedException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(ResponseCode.NOT_AUTHORIZED.httpStatus)
            .body(ApiResponse.error(ResponseCode.NOT_AUTHORIZED))
    }

    @ExceptionHandler(UnidentifiedUserException::class)
    fun handleForbiddenException(exception: UnidentifiedUserException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.error(exception.message))
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFoundException(exception: EntityNotFoundException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(exception.message))
    }
    @ExceptionHandler(ExceededRequestException::class)
    fun handleExceedRateLimitException(exception: ExceededRequestException): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .header(HttpHeaders.RETRY_AFTER, "1")
            .body(ApiResponse.error(exception.message))
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(exception: Exception): ResponseEntity<ApiResponse<Unit>> {
        logger.error("message", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(exception.message))
    }
}
