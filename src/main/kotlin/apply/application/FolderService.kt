package apply.application

import apply.domain.folder.*
import apply.domain.user.User
import apply.domain.user.UserSignedOutEvent
import apply.exception.CustomException
import apply.ui.api.ResponseCode
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.time.LocalDateTime


@Transactional
@Service
class FolderService(
    private val userService: UserService,
    private val linkService: LinkService,
    private val folderRepository: FolderRepository,
) {
    fun createBulk(user: User, request: List<SaveFolderRequest>) {
        val existingFolders = findAllByUserIdAndName(user.id, request.map { it.name })

        val notExisting = request.filterNot { requestEach -> existingFolders.any { existingEach ->
            requestEach.name == existingEach.name
        } }

        folderRepository.saveAll(notExisting.map {
            Folder(
                userId = user.id,
                name = it.name,
                visible = it.visible ?: false,
                createdDateTime = it.created_at ?: LocalDateTime.now()
            )
        })
    }

    fun create(uid: String, request: SaveFolderRequest) {
        val user = userService.getByUid(uid)
        if (folderRepository.existsByUserIdAndName(user.id, request.name)) {
            throw CustomException(ResponseCode.DUPLICATE_FOLDER_NAME)
        }
        folderRepository.save(
            Folder(
                userId = user.id,
                name = request.name,
                visible = request.visible ?: false,
                createdDateTime = request.created_at ?: LocalDateTime.now()
            )
        )
    }

    fun findAllByUserIdAndName(userId: Long, names: List<String>): List<Folder> {
        return folderRepository.findAllByUserIdAndNameIn(userId, names)
    }

    fun update(uid: String, folderId: Long, request: UpdateFolderRequest) {
        val user = userService.getByUid(uid)
        val folder = folderRepository.getById(folderId)
        if (folder.userId != user.id) throw CustomException(ResponseCode.NOT_AUTHORIZED_FOR_THE_DATA)
        request.name ?.let {
            folder.name = it
        }
        request.visible ?.let {
            folder.visible = it
        }
    }

    fun delete(uid: String, folderId: Long) {
        val user = userService.getByUid(uid)
        val folder = folderRepository.getById(folderId)
        if (folder.userId != user.id) throw CustomException(ResponseCode.NOT_AUTHORIZED_FOR_THE_DATA)
        linkService.deleteByFolder(folderId)
        folder.delete()
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun deleteSignedOutUsersFolders(event: UserSignedOutEvent) {
        folderRepository.deleteBatch(event.userId)
    }

    fun getByUserUid(uid: String): List<GetByUserIdResponse> {
        val user = userService.getByUid(uid)
        val folders = folderRepository.findAllByUserIdOrderByCreatedDateTime(user.id)
        val response = mutableListOf<GetByUserIdResponse>()
        response.add(linkService.countUnClassified(user.id).let {
            GetByUserIdResponse(
                name = "unclassified",
                visible = false,
                links = it
            )
        })
        folders.forEach {
            response.add(GetByUserIdResponse(
                id = it.id,
                name = it.name,
                thumbnail = it.thumbnail?.image,
                visible = it.visible,
                links = linkService.countByFolderId(it.id),
                created_date_time = it.createdDateTime
            ))
        }
        return response
    }

    fun getByUserId(id: Long): List<GetByUserIdResponse> {
        val user = userService.getById(id)
        return folderRepository.findVisibleByUserIdOrderByCreatedDateTime(user.id).map {
            GetByUserIdResponse(
                id = it.id,
                name = it.name,
                thumbnail = it.thumbnail?.image,
                visible = it.visible,
                links = linkService.countByFolderId(it.id),
                created_date_time = it.createdDateTime
            )
        }
    }

    fun getLinksByFolderId(uid: String, folderId: Long, pageRequest: PageRequest): Page<LinkResponse> {
        val user = userService.getByUid(uid)
        val folder = folderRepository.getById(folderId)
        if (folder.userId != user.id && !folder.visible)
            throw CustomException(ResponseCode.NOT_AUTHORIZED_FOR_THE_DATA)
        return linkService.getByFolderId(folderId, pageRequest)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun updateThumbnail(event: FolderLinkUpdatedEvent) {
        val folder = folderRepository.getById(event.folderId)
        event.thumbnail ?.let {
            folder.thumbnail = Thumbnail(it)
        } ?: run {
            linkService.getCurrentLinkByFolderId(event.folderId)?.image ?. let {
                folder.thumbnail = Thumbnail(it)
            } ?: run {
                folder.thumbnail = null
            }
        }
    }
}
