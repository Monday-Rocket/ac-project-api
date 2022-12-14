package linkpool.ui.api

import linkpool.application.ReportService
import linkpool.application.SaveReportRequest
import linkpool.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/reports")
@RestController
class ReportRestController(
    private val reportService: ReportService
) {
    @PostMapping
    fun report(
        @RequestBody request: SaveReportRequest,
        @LoginUser uid: String
    ): ResponseEntity<ApiResponse<Unit>> {
        reportService.create(uid, request)
        return ResponseEntity.ok(ApiResponse.success())
    }
}