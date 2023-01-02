package linkpool.ui.api

import linkpool.application.*
import linkpool.security.LoginUser
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/job-groups")
@RestController
class JobGroupRestController(
    private val jobGroupService: JobGroupService,
    private val linkService: LinkService
) {
    @GetMapping
    fun getJobGroups()
    : ResponseEntity<ApiResponse<List<JobGroupResponse>>> {
        return ResponseEntity.ok(ApiResponse.success(
                jobGroupService.getAll().map { JobGroupResponse(it) }))
    }

    @GetMapping("/{jobGroupId}/links")
    fun getLinkByJobGroups(
        @PathVariable("jobGroupId") jobGroupId: Long,
        @RequestParam(value = "page_no", required = false) pageNo: Int = 0,
        @RequestParam(value = "page_size", required = false) pageSize: Int = 10,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Page<LinkWithUserResponse>>> {
        return ResponseEntity.ok(ApiResponse.success(
            linkService.getLinksByJobGroupId(jobGroupId, PageRequest.of(pageNo, pageSize), uid) ))
    }
}