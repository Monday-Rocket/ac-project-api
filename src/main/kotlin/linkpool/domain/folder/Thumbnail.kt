package linkpool.domain.folder

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Thumbnail(
    @Column(length = 500)
    val image: String,
)
