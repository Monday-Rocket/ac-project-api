package apply.domain.link

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Lob

@SQLDelete(sql = "update link set deleted = true where id = ?")
@Where(clause = "deleted = false")
@Entity
class Link(
    @Column
    val userId: Long,

    @Column
    val folderId: Long? = null,

    @Column(nullable = false)
    val url: String,

    @Column
    val title: String? = null,

    @Column
    val image: String? = null,

    @Column
    val describe: String? = null,
    createdDateTime: LocalDateTime = LocalDateTime.now(),
    id: Long = 0L
) : BaseEntity(id, createdDateTime) {
    @Column(nullable = false)
    private var deleted: Boolean = false
}
