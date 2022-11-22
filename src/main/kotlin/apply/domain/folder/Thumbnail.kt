package apply.domain.folder

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Thumbnail(
    @Column
    val image: String,
)
