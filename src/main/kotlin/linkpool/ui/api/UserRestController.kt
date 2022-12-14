package linkpool.ui.api

import linkpool.application.*
import linkpool.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/users")
@RestController
class UserRestController(
    private val userService: UserService,
    private val folderService: FolderService
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
        return ResponseEntity.ok(ApiResponse.success(userService.getInformationByUid(uid)))
    }

    @GetMapping("/{userId}")
    fun getUserInfoById(
        @PathVariable("userId") userId: Long,
    ): ResponseEntity<ApiResponse<UserResponse>> {
        return ResponseEntity.ok(ApiResponse.success(userService.getInformationById(userId)))
    }

    @GetMapping("/{userId}/folders")
    fun getFoldersByUserId(
        @PathVariable("userId") userId: Long,
    ): ResponseEntity<ApiResponse<List<GetByUserIdResponse>>> {
        return ResponseEntity.ok(ApiResponse.success(folderService.getByUserId(userId)))
    }

    @DeleteMapping
    fun signOut(
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Unit>> {
        userService.signOut(uid)
        return ResponseEntity.ok(ApiResponse.success())
    }

}
