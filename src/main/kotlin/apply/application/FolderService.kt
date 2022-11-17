package apply.application

import apply.domain.folder.Folder
import apply.domain.folder.FolderRepository
import apply.domain.user.User

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Transactional
@Service
class FolderService(
    private val folderRepository: FolderRepository,
) {
    fun create(user: User, request: List<SaveFolderRequest>) {
        val existingFolders = findAllByName(request.map { it.name })

        val notExisting = request.filterNot { requestEach -> existingFolders.any { existingEach ->
            requestEach.name == existingEach.name
        } }

        folderRepository.saveAll(notExisting.map {
            Folder(
                userId = user.id,
                name = it.name,
                visible = it.visible ?: false,
                createdDateTime = it.created_date_time ?: LocalDateTime.now()
            )
        })
    }

    fun findAllByName(names: List<String>): List<Folder> {
        return folderRepository.findAllByNameIn(names)
    }

    fun updateThumbnail(folders: List<Folder>) {

    }

//    private fun findRecruitmentItemsToDelete(recruitmentId: Long, excludedItemIds: List<Long>): List<RecruitmentItem> {
//        return recruitmentItemRepository
//            .findByRecruitmentIdOrderByPosition(recruitmentId)
//            .filterNot { excludedItemIds.contains(it.id) }
//    }
//
//    fun findAllNotHidden(): List<RecruitmentResponse> {
//        return recruitmentRepository.findAllByHiddenFalse()
//            .map { RecruitmentResponse(it, termRepository.getById(it.termId)) }
//    }
//
//    fun findAll(): List<RecruitmentResponse> {
//        return recruitmentRepository.findAll()
//            .map { RecruitmentResponse(it, termRepository.getById(it.termId)) }
//    }
//
//    fun deleteById(id: Long) {
//        val recruitment = recruitmentRepository.getById(id)
//        check(!recruitment.recruitable) { "모집 중인 모집은 삭제할 수 없습니다." }
//        recruitmentRepository.delete(recruitment)
//    }
//
//    fun getById(id: Long): RecruitmentResponse {
//        val recruitment = recruitmentRepository.getById(id)
//        val term = termRepository.getById(recruitment.termId)
//        return RecruitmentResponse(recruitment, term)
//    }
//
//    fun getNotEndedDataById(id: Long): RecruitmentData {
//        val recruitment = recruitmentRepository.getById(id)
//        val term = termRepository.getById(recruitment.termId)
//        val recruitmentItems = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(recruitment.id)
//        return RecruitmentData(recruitment, term, recruitmentItems)
//    }
}
