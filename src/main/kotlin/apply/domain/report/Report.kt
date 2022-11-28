package apply.domain.report

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity

@SQLDelete(sql = "update report set deleted = true where id = ?")
@Where(clause = "deleted = false")
@Entity
class Report (
    @Column(nullable = false)
    var reporterId: Long,

    @Column(nullable = false)
    var reason: ReportReason,

    @Column(nullable = false)
    var target: ReportTarget,
    createdDateTime: LocalDateTime = LocalDateTime.now(),
    id: Long = 0L

): BaseEntity(id, createdDateTime) {
    @Column(nullable = false)
    private var deleted: Boolean = false

    fun delete() {
        deleted = true
    }
}
