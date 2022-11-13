package apply.application

import apply.domain.jobgroup.JobGroup
import apply.domain.user.User

data class JobGroupResponse(
    val id: Long,
    val name: String
) {
    constructor(jobGroup: JobGroup) : this(
        jobGroup.id,
        jobGroup.name,
    )
}