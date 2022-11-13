package apply.ui.api

import apply.application.AssignmentData
import apply.application.AssignmentRequest
import apply.application.AssignmentResponse
import apply.application.AssignmentService
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import support.toUri
import javax.validation.Valid

@RequestMapping("/api/recruitments/{recruitmentId}")
@RestController
class AssignmentRestController(
    private val assignmentService: AssignmentService
) {

    @GetMapping("/targets/{targetId}/assignments")
    fun findByEvaluationTargetId(
        @PathVariable recruitmentId: Long,
        @PathVariable targetId: Long,
        @LoginUser(administrator = true) user: User
    ): ResponseEntity<ApiResponse<AssignmentData>> {
        val assignments = assignmentService.findByEvaluationTargetId(targetId)
        return ResponseEntity.ok(ApiResponse.success(assignments))
    }
}
