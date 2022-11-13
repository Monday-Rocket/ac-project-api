package apply.application

import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.CheaterRepository
import apply.domain.evaluation.Evaluation
import apply.domain.evaluation.EvaluationRepository
import apply.domain.evaluation.getById
import apply.domain.evaluationitem.EvaluationItemRepository
import apply.domain.evaluationtarget.EvaluationAnswer
import apply.domain.evaluationtarget.EvaluationAnswers
import apply.domain.evaluationtarget.EvaluationStatus
import apply.domain.evaluationtarget.EvaluationTarget
import apply.domain.evaluationtarget.EvaluationTargetRepository
import apply.domain.evaluationtarget.getById
import apply.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class EvaluationTargetService(
    private val evaluationRepository: EvaluationRepository,
    private val evaluationTargetRepository: EvaluationTargetRepository,
    private val evaluationItemRepository: EvaluationItemRepository,
    private val applicationFormRepository: ApplicationFormRepository,
    private val userRepository: UserRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun findAllByEvaluationId(evaluationId: Long): List<EvaluationTarget> =
        evaluationTargetRepository.findAllByEvaluationId(evaluationId)

    private fun findLatestApplicants(evaluation: Evaluation): Set<Long> {
        return if (evaluation.hasBeforeEvaluation()) {
            findPassedUserIdsFromBeforeEvaluation(evaluation)
        } else {
            findUserIdsFromRecruitment(evaluation)
        }
    }

    private fun findPassedUserIdsFromBeforeEvaluation(evaluation: Evaluation): Set<Long> {
        return evaluationTargetRepository.findAllByEvaluationId(evaluation.beforeEvaluationId)
            .filter { it.isPassed }
            .map { it.userId }
            .toSet()
    }

    private fun findLoadedApplicants(evaluationId: Long): Set<Long> {
        return evaluationTargetRepository.findAllByEvaluationId(evaluationId)
            .map { it.userId }
            .toSet()
    }

    private fun findUserIdsFromRecruitment(evaluation: Evaluation): Set<Long> {
        return applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(evaluation.recruitmentId)
            .map { it.userId }
            .toSet()
    }


    private fun updateFail(userIds: Set<Long>, evaluation: Evaluation) {
        evaluationTargetRepository.findAllByEvaluationIdAndUserIdIn(evaluation.id, userIds).forEach {
            it.evaluationStatus = EvaluationStatus.FAIL
        }
    }

    fun getGradeEvaluation(targetId: Long): GradeEvaluationResponse {
        val evaluationTarget = evaluationTargetRepository.getById(targetId)
        val evaluation = evaluationRepository.getById(evaluationTarget.evaluationId)
        val evaluationItems = evaluationItemRepository.findByEvaluationIdOrderByPosition(evaluation.id)

        val evaluationItemScores = evaluationItems.map {
            EvaluationItemScoreData(
                id = it.id,
                score = evaluationTarget.evaluationAnswers.findScoreByEvaluationItemId(it.id)
            )
        }

        return GradeEvaluationResponse(
            title = evaluation.title,
            description = evaluation.description,
            evaluationTarget = EvaluationTargetData(
                note = evaluationTarget.note,
                evaluationStatus = evaluationTarget.evaluationStatus,
                evaluationItemScores = evaluationItemScores
            ),
            evaluationItems = evaluationItems.map { EvaluationItemResponse(it) }
        )
    }

    fun grade(evaluationTargetId: Long, request: EvaluationTargetData) {
        val evaluationTarget = evaluationTargetRepository.getById(evaluationTargetId)
        val evaluationAnswers = request.evaluationItemScores
            .map { EvaluationAnswer(it.score, it.id) }
            .toMutableList()
        evaluationTarget.update(
            evaluationStatus = request.evaluationStatus,
            evaluationAnswers = EvaluationAnswers(evaluationAnswers),
            note = request.note
        )
    }

    fun gradeAll(evaluationId: Long, evaluationTargetsData: Map<Long, EvaluationTargetData>) {
        evaluationTargetRepository.findAllByEvaluationId(evaluationId)
        evaluationTargetsData
            .forEach { (evaluationTargetId, evaluationTargetData) -> grade(evaluationTargetId, evaluationTargetData) }
    }
}
