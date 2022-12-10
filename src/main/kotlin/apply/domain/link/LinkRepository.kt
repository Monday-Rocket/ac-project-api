package apply.domain.link

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param

fun LinkRepository.getById(id: Long): Link = findByIdOrNull(id)
    ?: throw NoSuchElementException("링크가 존재하지 않습니다. id: $id")

interface LinkRepository : JpaRepository<Link, Long> {
    fun findAllByFolderId(folderId: Long): List<Link>
    fun findAllByUserId(userId: Long): List<Link>
    fun countByFolderId(folderId: Long): Int
    fun countByUserIdAndFolderIdIsNull(userId: Long): Int
    fun findPageByFolderIdOrderByCreatedDateTimeDesc(folderId: Long, pageable: Pageable): Page<Link>
    fun findPageByUserIdAndFolderIdIsNullOrderByCreatedDateTimeDesc(userId: Long, pageable: Pageable): Page<Link>
    fun existsByUserIdAndUrl(userId: Long, url: String): Boolean
    fun findPageByUserIdOrderByCreatedDateTimeDesc(id: Long, pageable: Pageable): Page<Link>

    @Query("""
        SELECT l FROM Link l
            JOIN FETCH Folder f ON l.folderId = f.id
            WHERE l.userId in :userIds
            AND f.visible = true
            AND l.id NOT IN 
            (
                SELECT r.target.targetId FROM Report r 
                    WHERE r.target.targetType = apply.domain.report.ReportTargetType.LINK
                    AND r.reporterId = :loggedInUserId
            )
            ORDER BY l.createdDateTime DESC
    """)
    fun findVisiblePageByUserIdIn(
        @Param("userIds") userIds: List<Long>,
        @Param("loggedInUserId") loggedInUserId: Long,
        pageable: Pageable
    ): Page<Link>
    fun findFirst1ByFolderIdOrderByCreatedDateTimeDesc(folderId: Long): Link?
    fun findPageByUserIdAndTitleContains(userId: Long, title: String, pageable: Pageable): Page<Link>
    @Query("""
        SELECT l FROM Link l
            JOIN FETCH Folder f ON l.folderId = f.id
            WHERE l.title like %:keyword%
            AND f.visible = true
            AND l.id NOT IN 
            (
                SELECT r.target.targetId FROM Report r 
                    WHERE r.target.targetType = apply.domain.report.ReportTargetType.LINK
                    AND r.reporterId = :loggedInUserId
            )
            ORDER BY l.createdDateTime DESC
    """)
    fun findPageByTitleContains(
        @Param("keyword") keyword: String,
        @Param("loggedInUserId") loggedInUserId: Long,
        pageable: Pageable
    ): Page<Link>

    @Modifying
    @Query("UPDATE Link l SET l.deleted = true WHERE l.userId = :userId")
    fun deleteBatch(@Param("userId") userId: Long)
}
