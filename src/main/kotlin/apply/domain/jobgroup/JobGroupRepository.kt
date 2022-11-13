package apply.domain.jobgroup

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

fun JobGroupRepository.getById(id: Long): JobGroup = findByIdOrNull(id)
    ?: throw NoSuchElementException("직업군이 존재하지 않습니다. id: $id")

interface JobGroupRepository: JpaRepository<JobGroup, Long> {
}