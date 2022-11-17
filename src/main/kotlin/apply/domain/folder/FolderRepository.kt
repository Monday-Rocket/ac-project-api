package apply.domain.folder

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun FolderRepository.getById(id: Long): Folder = findByIdOrNull(id)
    ?: throw NoSuchElementException("폴더가 존재하지 않습니다. id: $id")

interface FolderRepository : JpaRepository<Folder, Long> {
    fun findAllByVisibleFalse(): List<Folder>
    fun findAllByUserId(userId: Long): List<Folder>
    fun findAllByNameIn(names: List<String>): List<Folder>
}
