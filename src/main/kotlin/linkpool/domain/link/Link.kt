package linkpool.domain.link

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity

@SQLDelete(sql = "update link set deleted = true where id = ?")
@Where(clause = "deleted = false")
@Entity
class Link(
    @Column
    val userId: Long,

    @Column
    var folderId: Long? = null,

    @Column(nullable = false, length = 500)
    var url: String,

    @Column(length = 30)
    var title: String? = null,

    @Column(length = 500)
    var image: String? = null,

    @Column(length = 550)
    var describe: String? = null,
    createdDateTime: LocalDateTime = LocalDateTime.now(),
    id: Long = 0L
) : BaseEntity(id, createdDateTime) {
    @Column(nullable = false)
    private var deleted: Boolean = false

    fun delete() {
        deleted = true
    }
}
