package apply.application

import apply.domain.link.Link
import apply.domain.link.LinkRepository
import apply.domain.link.getById
import apply.domain.user.User
import apply.exception.CustomException
import apply.ui.api.ResponseCode
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class LinkService(
    private val userService: UserService,
    private val linkRepository: LinkRepository
    ) {
    fun createBulk(user: User, request: List<SaveLinkRequest>) {
        linkRepository.saveAll(request.map {
            Link(
                userId = user.id,
                url = it.url,
                title = it.title,
                describe = it.describe,
                image = it.image,
                folderId = it.folder_id,
                createdDateTime = it.created_date_time ?: LocalDateTime.now()
            )
        })
    }

    fun countByFolderId(folderId: Long)
        = linkRepository.countByFolderId(folderId)

    fun countUnClassified(userId: Long)
        = linkRepository.countByUserIdAndFolderIdIsNull(userId)

    fun getByFolderId(folderId: Long, pageRequest: PageRequest): Page<LinkResponse> {
        return linkRepository.findPageByFolderIdOrderByCreatedDateTimeDesc(folderId, pageRequest).let {
            Page(
                page_no = it.number,
                page_size = it.size,
                total_count = it.numberOfElements,
                total_page = it.totalPages,
                contents = it.content.map { link ->
                    LinkResponse(
                        id = link.id,
                        url = link.url,
                        title = link.title,
                        image = link.image,
                        folder_id = link.folderId,
                        describe = link.describe,
                        created_date_time = link.createdDateTime,
                    )
                }
            )
        }
    }

    fun getUnclassifiedLinks(uid: String, pageRequest: PageRequest): Page<LinkResponse>? {
        val user = userService.getByUid(uid)
        return linkRepository.findPageByUserIdAndFolderIdIsNullOrderByCreatedDateTimeDesc(user.id, pageRequest).let {
            Page(
                page_no = it.number,
                page_size = it.size,
                total_count = it.numberOfElements,
                total_page = it.totalPages,
                contents = it.content.map { link ->
                    LinkResponse(
                        id = link.id,
                        url = link.url,
                        title = link.title,
                        image = link.image,
                        folder_id = link.folderId,
                        describe = link.describe,
                        created_date_time = link.createdDateTime,
                    )
                }
            )
        }
    }

    fun create(uid: String, request: SaveLinkRequest) {
        val user = userService.getByUid(uid)
        if (linkRepository.existsByUserIdAndUrl(user.id, request.url)) {
            throw CustomException(ResponseCode.DUPLICATE_LINK_URL)
        }
        linkRepository.save(
            Link(
                userId = user.id,
                url = request.url,
                title = request.title,
                describe = request.describe,
                image = request.image,
                folderId = request.folder_id,
                createdDateTime = request.created_date_time ?: LocalDateTime.now()
            )
        )
    }

    fun update(uid: String, linkId: Long, request: UpdateLinkRequest) {
        val user = userService.getByUid(uid)
        var link = linkRepository.getById(linkId)
        if (link.userId != user.id) throw CustomException(ResponseCode.NOT_AUTHORIZED_FOR_THE_DATA)
        request.url ?.let {
            link.url = it
        }
        request.title ?.let {
            link.title = it
        }
        request.describe ?.let {
            link.describe = it
        }
        request.image ?.let {
            link.image = it
        }
        request.folder_id ?.let {
            link.folderId = it
        }
    }

    fun delete(uid: String, linkId: Long) {
        val user = userService.getByUid(uid)
        val link = linkRepository.getById(linkId)
        if (link.userId != user.id) throw CustomException(ResponseCode.NOT_AUTHORIZED_FOR_THE_DATA)
        link.delete()
    }

    fun getByUserId(uid: String, pageRequest: PageRequest): Page<LinkResponse> {
        val user = userService.getByUid(uid)
        return linkRepository.findPageByUserIdOrderByCreatedDateTimeDesc(user.id, pageRequest).let {
            Page(
                page_no = it.number,
                page_size = it.size,
                total_count = it.numberOfElements,
                total_page = it.totalPages,
                contents = it.content.map { link ->
                    LinkResponse(
                        id = link.id,
                        url = link.url,
                        title = link.title,
                        image = link.image,
                        folder_id = link.folderId,
                        describe = link.describe,
                        created_date_time = link.createdDateTime,
                    )
                }
            )
        }
    }
}
