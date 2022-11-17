package apply.domain.link

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun LinkRepository.getById(id: Long): Link = findByIdOrNull(id)
    ?: throw NoSuchElementException("폴더가 존재하지 않습니다. id: $id")

interface LinkRepository : JpaRepository<Link, Long> {
    fun findAllByFolderId(folderId: Long): List<Link>
    fun findAllByUserId(userId: Long): List<Link>
}
