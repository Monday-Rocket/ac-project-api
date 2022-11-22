package apply.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class BulkService(
    private val userService: UserService,
    private val folderService: FolderService,
    private val linkService: LinkService
) {
    fun create(uid: String, request: BulkCreateRequest) {
        val user = userService.getByUid(uid)
        folderService.createBulk(user, request.new_folders)
        val folders = folderService.findAllByUserIdAndName(user.id, request.new_links.filter { it.folder_name != null }.map { it.folder_name!! })

        linkService.createBulk(user, request.new_links.map {
            SaveLinkRequest(
                url = it.url,
                title = it.title,
                describe = it.describe,
                image = it.image,
                folder_id = folders.find { folder -> it.folder_name == folder.name }?.id,
                created_date_time = it.created_date_time
            )
        })
    }
}