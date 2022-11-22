package apply.application

import apply.domain.jobgroup.JobGroup
import apply.domain.user.Password
import apply.domain.user.User
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.Past
import javax.validation.constraints.Pattern

data class UserResponse(
    val id: Long,
    val nickname: String?,
    val job_group: JobGroupResponse?,
    val profile_img: String?
) {
    constructor(user: User, jobGroup: JobGroup) : this(
        user.id,
        user.info?.nickname,
        JobGroupResponse(jobGroup),
        user.info?.profileImage
    )
}


data class CreateUserResponse(
    val id: Long,
    val is_new: Boolean
)

data class UpdateUserRequest(
    val nickname: String?,
    val job_group_id: Long?,
    val profile_img: String?
)

data class AuthenticateUserRequest(
    @field:Email
    val email: String,
    val password: Password
)

data class ResetPasswordRequest(
    @field:Pattern(regexp = "[가-힣]{1,30}", message = "올바른 형식의 이름이어야 합니다")
    val name: String,

    @field:Email
    val email: String,

    @field:Past
    val birthday: LocalDate
)

data class EditPasswordRequest(
    val oldPassword: Password,
    val password: Password,
    val confirmPassword: Password
)

data class EditInformationRequest(
    @field:Pattern(regexp = "010-\\d{4}-\\d{4}", message = "올바른 형식의 전화번호여야 합니다")
    val phoneNumber: String
)
