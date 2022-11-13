package apply.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun UserRepository.getByUid(uid: String): User = findByInfoUid(uid)
    ?: throw NoSuchElementException("회원이 존재하지 않습니다. uid: $uid")

fun UserRepository.getById(id: Long): User = findByIdOrNull(id)
    ?: throw NoSuchElementException("회원이 존재하지 않습니다. id: $id")

interface UserRepository : JpaRepository<User, Long> {
    fun findByInfoUid(uid: String): User?
}