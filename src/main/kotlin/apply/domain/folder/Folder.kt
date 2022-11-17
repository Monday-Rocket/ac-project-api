package apply.domain.folder

import org.apache.poi.hpsf.Thumbnail
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

    @Embedded
    var thumbnail: Thumbnail? = null,

    visible: Boolean = false,
    id: Long = 0L,
    createdDateTime: LocalDateTime = LocalDateTime.now(),
) : BaseEntity(id, createdDateTime) {
    @Column(nullable = false)
    var visible: Boolean = visible
        private set

    @Column(nullable = false)
    private var deleted: Boolean = false

}
