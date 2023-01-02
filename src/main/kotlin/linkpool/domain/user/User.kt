package linkpool.domain.user

import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.Where
import support.domain.BaseEntity
import javax.persistence.*


@SQLDelete(sql = "update `user` set deleted = true where id = ?")
@Where(clause = "deleted = false")
@Table(
    name = "user",
    indexes = [Index(name = "ix_user_uid", columnList = "uid")]
)
@Entity
class User(
    @Column(nullable = false, unique = true)
    val uid: String,
    @Embedded
    var info: UserInformation? = null,
    id: Long = 0L
) : BaseEntity(id) {

    @Column(nullable = false)
    private var deleted: Boolean = false

    fun checkSignedUp(): Boolean {
        return info != null
    }

    fun delete() {
        info = null
        deleted = true
    }

    fun activate() {
        deleted = false
    }
}

