package linkpool.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param

fun UserRepository.getByUid(uid: String): User = findByUid(uid)
    ?: throw NoSuchElementException("회원이 존재하지 않습니다. uid: $uid")

fun UserRepository.getById(id: Long): User = findByIdOrNull(id)
    ?: throw NoSuchElementException("회원이 존재하지 않습니다. id: $id")

interface UserRepository : JpaRepository<User, Long> {
    fun findByUid(uid: String): User?

    @Query("""
        SELECT u FROM User u
            WHERE u.id NOT IN 
            (
                SELECT r.target.targetId FROM Report r 
                    WHERE r.target.targetType = linkpool.domain.report.ReportTargetType.USER
                    AND r.reporterId = :userId
            )
            AND u.info.jobGroupId = :jobGroupId
    """)
    fun findAllByInfoJobGroupId(
        @Param("jobGroupId") jobGroupId: Long,
        @Param("userId") userId: Long)
    : List<User>
    @Query("""
        SELECT * FROM user
            WHERE deleted = true
            AND uid = :uid
    """, nativeQuery = true)
    fun findSignedOutUserByUid(uid: String): User?
}
