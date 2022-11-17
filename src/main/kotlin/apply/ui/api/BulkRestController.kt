package apply.ui.api

import apply.application.*
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import support.toUri

@RequestMapping("/bulk")
@RestController
class BulkRestController(
    private val bulkService: BulkService
) {
    @PostMapping
    fun create(
        @RequestBody request: BulkCreateRequest,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Unit>> {
        bulkService.create(uid, request)
        return ResponseEntity.ok(ApiResponse.success())
    }
}
