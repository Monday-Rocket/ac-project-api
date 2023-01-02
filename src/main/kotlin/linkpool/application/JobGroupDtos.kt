package linkpool.application

import linkpool.domain.jobgroup.JobGroup

data class JobGroupResponse(
    val id: Long,
    val name: String
) {
    constructor(jobGroup: JobGroup) : this(
        jobGroup.id,
        jobGroup.name,
    )
}