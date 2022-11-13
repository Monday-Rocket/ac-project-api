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
        return userRepository.findByInfoUid(uid) ?: throw IllegalArgumentException("회원이 존재하지 않습니다. uid: $uid")
    }

    fun createUser(uid: String): CreateUserResponse {
        userRepository.findByInfoUid(uid) ?.let {
            return CreateUserResponse(id = it.id, is_new = false)
        }
        userRepository.save(User(UserInformation(uid))).let {
            return CreateUserResponse(id = it.id, is_new = true)
        }
    }

    fun updateUserInfo(uid: String, updateUserRequest: UpdateUserRequest): UserResponse {
        userRepository.findByInfoUid(uid) ?.let {
            it.info.nickname = updateUserRequest.nickname
            it.info.JobGroupId = updateUserRequest.job_group_id
            it.info.profileImage = updateUserRequest.profile_img
            return UserResponse(it, jobGroupService.getJobGroupById(updateUserRequest.job_group_id))
        } ?: throw IllegalArgumentException("회원이 존재하지 않습니다. uid: $uid")
    }

    fun getInformation(uid: String): UserResponse {
        return userRepository.findByInfoUid(uid) ?.let {
            return UserResponse(
                it,
                jobGroupService.getJobGroupById(
                    it.info.JobGroupId ?: throw CustomException(ResponseCode.NOT_SIGNED_UP)
                )
            )
        } ?: throw IllegalArgumentException("회원이 존재하지 않습니다. uid: $uid")
    }
}
