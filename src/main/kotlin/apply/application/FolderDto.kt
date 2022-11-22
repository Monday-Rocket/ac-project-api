package apply.application

import apply.domain.folder.Thumbnail
import java.time.LocalDateTime


data class SaveFolderRequest(
    val name: String,
    val visible: Boolean? = false,
    val created_date_time: LocalDateTime? = null
)

data class UpdateFolderRequest(
    val name: String? = null,
    val visible: Boolean? = null,
)

data class GetByUserIdResponse(
    val id: Long? = null,
    val name: String,
    val thumbnail: String? = null,
    val visible: Boolean? = false,
    val links: Int,
    val created_date_time: LocalDateTime? = null,
)