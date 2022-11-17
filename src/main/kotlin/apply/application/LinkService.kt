package apply.application

import apply.domain.link.Link
import apply.domain.link.LinkRepository
import apply.domain.user.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class LinkService(
    private val folderService: FolderService,
    private val linkRepository: LinkRepository
    ) {
    fun createWithFolderName(user: User, request: List<SaveLinkWithFolderNameRequest>) {
        val folders = folderService.findAllByName(request.filter { it.folder_name != null }.map { it.folder_name!! })
        linkRepository.saveAll(request.map {
            Link(
                userId = user.id,
                url = it.url,
                title = it.title,
                describe = it.describe,
                image = it.image,
                folderId = folders.find { folder -> folder.name == it.folder_name }?.id,
                createdDateTime = it.created_date_time ?: LocalDateTime.now()
            )
        })
    }
}
