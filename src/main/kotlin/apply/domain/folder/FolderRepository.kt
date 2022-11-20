package apply.domain.folder

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param

fun FolderRepository.getById(id: Long): Folder = findByIdOrNull(id)
    ?: throw NoSuchElementException("폴더가 존재하지 않습니다. id: $id")

interface FolderRepository : JpaRepository<Folder, Long> {
    fun findAllByVisibleFalse(): List<Folder>
    fun findAllByUserIdOrderByCreatedDateTime(userId: Long): List<Folder>
    @Query("SELECT f FROM Folder f WHERE f.userId = :userId AND name in :names")
    fun findAllByUserIdAndNameIn(@Param("userId") userId: Long, @Param("names") names: List<String>): List<Folder>
    fun existsByUserIdAndName(userId: Long, name: String): Boolean
}
