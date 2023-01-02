package linkpool.domain.folder

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity

@SQLDelete(sql = "update folder set deleted = true where id = ?")
@Where(clause = "deleted = false")
@Entity
class Folder(
    @Column
    val userId: Long,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var visible: Boolean = false,

    @Embedded
    var thumbnail: Thumbnail? = null,
    id: Long = 0L,
    createdDateTime: LocalDateTime = LocalDateTime.now(),
) : BaseEntity(id, createdDateTime) {

    @Column(nullable = false)
    private var deleted: Boolean = false

    fun delete() {
        deleted = true
    }

}
