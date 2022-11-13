package apply.domain.user

import java.time.LocalDate
import javax.persistence.*

@Embeddable
data class UserInformation(

    @Column(nullable = false, unique = true)
    val uid: String,

    @Column(length = 30)
    var nickname: String? = null,

    @Column
    var JobGroupId: Long? = null,

    @Column
    var profileImage: String? = null,
)
