package apply.security

import apply.exception.CustomException
import apply.ui.api.ResponseCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

private const val BEARER = "Bearer"

class LoginUserResolver(
    private val firebaseAuth: FirebaseAuth
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): String {
        validateIfAdministrator(parameter)
        val token = extractBasicToken(webRequest)
        if (!isAuthenticated(token, parameter)) {
            throw CustomException(ResponseCode.NOT_AUTHORIZED)
        }
        return getUid(token, parameter)
    }

    private fun validateIfAdministrator(parameter: MethodParameter) {
        val annotation = parameter.getParameterAnnotation(LoginUser::class.java)
        if (annotation?.administrator == true) {
            // TODO: 관리자가 HTTP API를 사용할 때 작업
            throw LoginFailedException()
        }
    }

    private fun isAuthenticated(token: String, parameter: MethodParameter): Boolean {
        try {
            firebaseAuth.verifyIdToken(token)
        } catch (e: FirebaseAuthException) {
            return false
        }
        return true
    }

    private fun getUid(token: String, parameter: MethodParameter): String {
        return firebaseAuth.verifyIdToken(token).uid
    }


    private fun extractBasicToken(request: NativeWebRequest): String {
        return request.getHeader("x-auth-token") ?: throw CustomException(ResponseCode.NOT_AUTHORIZED)
    }

    private fun splitToTokenFormat(authorization: String): Pair<String, String> {
        return try {
            val tokenFormat = authorization.split(" ")
            tokenFormat[0] to tokenFormat[1]
        } catch (e: IndexOutOfBoundsException) {
            throw LoginFailedException()
        }
    }
}
