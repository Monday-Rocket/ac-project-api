package linkpool.domain.report

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class ReportReason (

    @Column(nullable = false)
    var reason: ReportReasonType,

    @Column(length = 500)
    var otherReason: String? = null,
)