package com.codefactory.dev_social_network.comentarios.repository;

import com.codefactory.dev_social_network.comentarios.entity.CommentVoteEntity;
import com.codefactory.dev_social_network.comentarios.entity.CommentVoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CommentVoteJpaRepository extends JpaRepository<CommentVoteEntity, CommentVoteId> {

    boolean existsByCommentIdAndUserId(UUID commentId, UUID userId);

    long countByCommentId(UUID commentId);

    @Modifying
    @Query("delete from CommentVoteEntity v where v.commentId = :commentId and v.userId = :userId")
    int deleteVote(@Param("commentId") UUID commentId, @Param("userId") UUID userId);

    /** Filas [commentId, total] para varios comentarios en una sola consulta (evita N+1). */
    @Query("select v.commentId, count(v) from CommentVoteEntity v "
            + "where v.commentId in :commentIds group by v.commentId")
    List<Object[]> countByCommentIds(@Param("commentIds") Collection<UUID> commentIds);

    @Query("select v.commentId from CommentVoteEntity v "
            + "where v.userId = :userId and v.commentId in :commentIds")
    List<UUID> findVotedCommentIds(@Param("userId") UUID userId,
                                   @Param("commentIds") Collection<UUID> commentIds);
}