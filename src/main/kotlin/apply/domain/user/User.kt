package apply.domain.user

import support.domain.BaseEntity
import javax.persistence.*


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
) : BaseEntity(id)

