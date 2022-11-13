package apply.ui.api

import apply.application.*
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RequestMapping("/users")
@RestController
class UserRestController(
    private val userService: UserService,
) {

    @PostMapping
    fun signUp(@LoginUser uid: String): ResponseEntity<ApiResponse<CreateUserResponse>> {
        val token = userService.createUser(uid)
        return ResponseEntity.ok(ApiResponse.success(token))
    }

    @PatchMapping
    fun updateMyInfo(
        @RequestBody updateUserRequest: UpdateUserRequest,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<UserResponse>> {
        return ResponseEntity.ok(ApiResponse.success(userService.updateUserInfo(uid, updateUserRequest)))
    }

    @GetMapping
    fun getMyInformation(
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<UserResponse>> {
        return ResponseEntity.ok(ApiResponse.success(userService.getInformation(uid)))
    }

}
