package apply.domain.report

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class ReportTarget (
    @Column(nullable = false)
    var targetType: ReportTargetType,

    @Column(nullable = false)
    var targetId: Long,
)
