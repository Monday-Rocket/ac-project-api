package linkpool.application

import linkpool.domain.jobgroup.JobGroup
import linkpool.domain.jobgroup.JobGroupRepository
import linkpool.domain.jobgroup.getById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class JobGroupService(
    private val jobGroupRepository: JobGroupRepository,
) {

    fun getAll(): List<JobGroup> {
        return jobGroupRepository.findAll()
    }

    fun getById(id: Long): JobGroup {
        return jobGroupRepository.getById(id)
    }
}