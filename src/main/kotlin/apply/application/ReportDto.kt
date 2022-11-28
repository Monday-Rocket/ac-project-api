package apply.application

import apply.domain.report.ReportReasonType
import apply.domain.report.ReportTargetType

data class SaveReportRequest (
    val targetType: ReportTargetType,
    val targetId: Long,
    val reasonType: ReportReasonType,
    val otherReason: String? = null,
)

