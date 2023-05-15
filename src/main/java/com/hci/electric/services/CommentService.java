package com.hci.electric.services;

import java.util.List;

import com.hci.electric.models.Comment;

public interface CommentService {
    public Comment save(Comment comment);
    public List<Comment> getCommentsByProduct(String productId);
    public List<Comment> getRepliesOfComment(String commentId);
    public List<Comment> paginateWithProduct(String productId, int page, int num, boolean sortByNewest);
}
