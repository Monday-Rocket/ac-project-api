package linkpool.application

import linkpool.domain.report.ReportReasonType
import linkpool.domain.report.ReportTargetType

data class SaveReportRequest (
    val targetType: ReportTargetType,
    val targetId: Long,
    val reasonType: ReportReasonType,
    val otherReason: String? = null,
)

