package com.hci.electric.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hci.electric.models.Comment;
import com.hci.electric.utils.queries.CommentQuery;

public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query(value = CommentQuery.queryGetCommentsOfProduct, nativeQuery = true)
    Optional<List<Comment>> getCommentsOfProduct(String productId);

    @Query(value = CommentQuery.queryGetRepliesOfComment, nativeQuery = true)
    Optional<List<Comment>> getRepliesOfComment(String commentId);
}
