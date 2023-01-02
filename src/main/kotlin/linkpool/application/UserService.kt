package linkpool.application

import linkpool.domain.user.*
import linkpool.exception.CustomException
import linkpool.ui.api.ResponseCode
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserService(
    private val userRepository: UserRepository,
    private val jobGroupService: JobGroupService,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun getByUid(uid: String): User {
        val user = userRepository.findByUid(uid)
            ?: throw IllegalArgumentException("회원이 존재하지 않습니다. uid: $uid")
        if (!user.checkSignedUp()) throw CustomException(ResponseCode.NOT_SIGNED_UP)
        return user;
    }

    fun createUser(uid: String): CreateUserResponse {
        userRepository.findByUid(uid) ?.let {
            if (it.info != null) {
                return CreateUserResponse(id = it.id, is_new = false)
            } else {
                return CreateUserResponse(id = it.id, is_new = true)
            }
        }
        userRepository.findSignedOutUserByUid(uid) ?. let {
            it!!.activate()
            return CreateUserResponse(id = it.id, is_new = true)
        }

        userRepository.save(User(uid)).let {
            return CreateUserResponse(id = it.id, is_new = true)
        }
    }

    fun updateUserInfo(uid: String, updateUserRequest: UpdateUserRequest): UserResponse {
        userRepository.findByUid(uid) ?.let {
            it.info ?.let { info ->
                info.nickname = updateUserRequest.nickname ?: info.nickname
                info.jobGroupId = updateUserRequest.job_group_id ?: info.jobGroupId
                info.profileImage = updateUserRequest.profile_img ?: info.profileImage
                return UserResponse(it, jobGroupService.getById(info.jobGroupId))
            } ?: run {
                it.info = UserInformation(
                    nickname = updateUserRequest.nickname ?: throw CustomException(ResponseCode.NOT_ENOUGH_FOR_SIGNING_UP),
                    jobGroupId = updateUserRequest.job_group_id ?: throw CustomException(ResponseCode.NOT_ENOUGH_FOR_SIGNING_UP),
                    profileImage = updateUserRequest.profile_img ?: throw CustomException(ResponseCode.NOT_ENOUGH_FOR_SIGNING_UP)
                )
                return UserResponse(it, jobGroupService.getById(updateUserRequest.job_group_id))
            }
        } ?: throw IllegalArgumentException("회원이 존재하지 않습니다. uid: $uid")
    }

    fun getInformationByUid(uid: String): UserResponse {
        return userRepository.findByUid(uid) ?.let {
            return UserResponse(
                it,
                jobGroupService.getById(
                    it.info?.jobGroupId ?: throw CustomException(ResponseCode.NOT_SIGNED_UP)
                )
            )
        } ?: throw IllegalArgumentException("회원이 존재하지 않습니다. uid: $uid")
    }

    fun getByJobGroupId(jobGroupId: Long, userId: Long): List<User> {
        return userRepository.findAllByInfoJobGroupId(jobGroupId, userId)
    }

    fun getById(id: Long): User {
        return userRepository.getById(id)
    }

    fun getInformationById(userId: Long): UserResponse? {
        return userRepository.getById(userId).let {
            return UserResponse(
                it,
                jobGroupService.getById(
                    it.info?.jobGroupId ?: throw CustomException(ResponseCode.NOT_SIGNED_UP)
                )
            )
        }
    }

    fun signOut(uid: String) {
        val user = getByUid(uid)
        applicationEventPublisher.publishEvent(UserSignedOutEvent(user.id))
        user.delete()
    }
}
