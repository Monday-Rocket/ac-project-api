package apply.ui.api

import apply.application.JobGroupResponse
import apply.application.JobGroupService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/job-groups")
@RestController
class JobGroupRestController(
    private val jobGroupService: JobGroupService
) {

    @GetMapping
    fun getJobGroups()
    : ResponseEntity<ApiResponse<List<JobGroupResponse>>> {
        return ResponseEntity.ok(ApiResponse.success(
                jobGroupService.getAll().map { JobGroupResponse(it) }))
    }
}