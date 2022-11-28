package apply.application

import apply.domain.report.*
import apply.exception.CustomException
import apply.ui.api.ResponseCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ReportService(
    private val reportRepository: ReportRepository,
    private val userService: UserService
) {
    fun create(uid: String, request: SaveReportRequest) {
        val user = userService.getByUid(uid)
        val target = ReportTarget(
            targetType = request.targetType,
            targetId = request.targetId
        )
        if (getByReportIdAndTarget(user.id, target) != null)
            throw CustomException(ResponseCode.DUPLICATE_REPORT)
        reportRepository.save(
            Report(
                reporterId = user.id,
                reason = ReportReason(
                    reason = request.reasonType,
                    otherReason = request.otherReason
                ),
                target = target
            )
        )
    }

    fun getByReportIdAndTarget(reporterId: Long, target: ReportTarget): Report? {
        return reportRepository.findByReporterIdAndTarget(reporterId, target)
    }

}