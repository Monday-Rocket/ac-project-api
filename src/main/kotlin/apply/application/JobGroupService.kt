package apply.application

import apply.domain.jobgroup.JobGroup
import apply.domain.jobgroup.JobGroupRepository
import apply.domain.jobgroup.getById
import apply.domain.user.User
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.awt.print.Pageable

@Transactional
@Service
class JobGroupService(
    private val jobGroupRepository: JobGroupRepository,
) {

    fun getAll(): List<JobGroup> {
        return jobGroupRepository.findAll()
    }

    fun getById(id: Long): JobGroup {
        return jobGroupRepository.getById(id) ?: throw IllegalArgumentException("직업군이 존재하지 않습니다. uid: $id")
    }
}