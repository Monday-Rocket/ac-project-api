package linkpool.application

import linkpool.domain.jobgroup.JobGroup
import linkpool.domain.link.Link
import linkpool.domain.user.User
import java.time.LocalDateTime

data class SaveLinkWithFolderNameRequest(
    val url: String,
    val title: String? = null,
    val describe: String? = null,
    val image: String? = null,
    val folder_name: String? = null,
    val created_at: LocalDateTime? = null
)

data class SaveLinkRequest(
    val url: String,
    val title: String? = null,
    val describe: String? = null,
    val image: String? = null,
    val folder_id: Long? = null,
    val created_at: LocalDateTime? = null
)

data class UpdateLinkRequest(
    val url: String? = null,
    val title: String? = null,
    val describe: String? = null,
    val image: String? = null,
    val folder_id: Long? = null,
)


data class LinkResponse(
    val id: Long,
    val url: String,
    val title: String? = null,
    val image: String? = null,
    val folder_id: Long? = null,
    val describe: String? = null,
    val created_date_time: LocalDateTime,
)

data class LinkWithUserResponse(
    val id: Long,
    val user: UserResponse,
    val url: String,
    val title: String? = null,
    val image: String? = null,
    val folder_id: Long? = null,
    val describe: String? = null,
    val created_date_time: LocalDateTime,
) {
    constructor(user: User, jobGroup: JobGroup, link: Link) : this(
        link.id,
        UserResponse(user, jobGroup),
        link.url,
        link.title,
        link.image,
        link.folderId,
        link.describe,
        link.createdDateTime
    )
}