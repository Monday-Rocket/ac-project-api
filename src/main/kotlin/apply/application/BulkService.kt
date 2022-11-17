package apply.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class BulkService(
    private val userService: UserService,
    private val folderService: FolderService,
    private val linkService: LinkService
) {
    fun create(uid: String, request: BulkCreateRequest) {
        val user = userService.getByUid(uid)
        val folders = folderService.create(user, request.new_folders)
        linkService.createWithFolderName(user, request.new_links)
//        folderService.updateThumbnail(folders)
    }
}