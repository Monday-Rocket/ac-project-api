package linkpool.domain.user

import javax.persistence.*

@Embeddable
data class UserInformation(

    @Column(length = 30)
    var nickname: String,

    @Column
    var jobGroupId: Long,

    @Column
    var profileImage: String? = null,
)
