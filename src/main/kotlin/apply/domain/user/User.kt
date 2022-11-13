package apply.domain.user

import support.domain.BaseEntity
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table


@Table(
    name = "user",
    indexes = [Index(name = "ix_user_uid", columnList = "uid")]
)
@Entity
class User(
    @Embedded
    val info: UserInformation,
    id: Long = 0L
) : BaseEntity(id)

