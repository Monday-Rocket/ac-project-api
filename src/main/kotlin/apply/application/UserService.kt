package apply.application

import apply.domain.user.User
import apply.domain.user.UserInformation
import apply.domain.user.UserRepository
import apply.exception.CustomException
import apply.ui.api.ResponseCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserService(
    private val userRepository: UserRepository,
    private val jobGroupService: JobGroupService
) {

    fun getByUid(uid: String): User {
        return userRepository.findByUid(uid) ?: throw IllegalArgumentException("회원이 존재하지 않습니다. uid: $uid")
    }

    fun createUser(uid: String): CreateUserResponse {
        userRepository.findByUid(uid) ?.let {
            if (it.info != null) {
                return CreateUserResponse(id = it.id, is_new = false)
            } else {
                return CreateUserResponse(id = it.id, is_new = true)
            }
        }
        userRepository.save(User(uid)).let {
            return CreateUserResponse(id = it.id, is_new = true)
        }
    }

    fun updateUserInfo(uid: String, updateUserRequest: UpdateUserRequest): UserResponse {
        userRepository.findByUid(uid) ?.let {
            it.info = UserInformation(
                nickname = updateUserRequest.nickname,
                jobGroupId = updateUserRequest.job_group_id,
                profileImage = updateUserRequest.profile_img
            )
            return UserResponse(it, jobGroupService.getJobGroupById(updateUserRequest.job_group_id))
        } ?: throw IllegalArgumentException("회원이 존재하지 않습니다. uid: $uid")
    }

    fun getInformation(uid: String): UserResponse {
        return userRepository.findByUid(uid) ?.let {
            return UserResponse(
                it,
                jobGroupService.getJobGroupById(
                    it.info?.jobGroupId ?: throw CustomException(ResponseCode.NOT_SIGNED_UP)
                )
            )
        } ?: throw IllegalArgumentException("회원이 존재하지 않습니다. uid: $uid")
    }
}
