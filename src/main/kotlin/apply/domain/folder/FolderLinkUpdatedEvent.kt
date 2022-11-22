package apply.domain.folder

import org.springframework.context.ApplicationEvent

class FolderLinkUpdatedEvent(
    val folderId: Long,
    val thumbnail: String? = null
): ApplicationEvent(folderId)