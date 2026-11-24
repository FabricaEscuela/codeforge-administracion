package com.codefactory.dev_social_network.comentarios.repository;

import com.codefactory.dev_social_network.comentarios.domain.CommentReplyCount;
import com.codefactory.dev_social_network.comentarios.entity.CommentReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CommentReplyJpaRepository extends JpaRepository<CommentReplyEntity, UUID> {

    List<CommentReplyEntity> findByCommentIdOrderByCreatedAtAsc(UUID commentId);

    long countByCommentId(UUID commentId);

    @Query("""
            select new com.codefactory.dev_social_network.comentarios.domain.CommentReplyCount(r.commentId, count(r))
            from CommentReplyEntity r
            where r.commentId in (select c.id from ProjectCommentEntity c where c.projectId = :projectId)
            group by r.commentId
            """)
    List<CommentReplyCount> countGroupedByProject(@Param("projectId") UUID projectId);
}