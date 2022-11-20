package apply.domain.link

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

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
}
