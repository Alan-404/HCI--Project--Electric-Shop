package com.hci.electric.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.hci.electric.models.Comment;
import com.hci.electric.repositories.CommentRepository;
import com.hci.electric.services.CommentService;
import com.hci.electric.utils.Constants;
import com.hci.electric.utils.Libraries;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment){
        try{
            comment.setId(Libraries.generateId(Constants.lengthId));
            comment.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            comment.setModifiedAt(new Timestamp(System.currentTimeMillis()));

            return this.commentRepository.save(comment);
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Comment> getCommentsByProduct(String productId){
        try{
            Optional<List<Comment>> comments = this.commentRepository.getCommentsOfProduct(productId);
            if (comments.isPresent() == false){
                return new ArrayList<>();
            }

            return comments.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Comment> getRepliesOfComment(String commentId){
        try{
            Optional<List<Comment>> replies = this.commentRepository.getRepliesOfComment(commentId);
            if (replies.isPresent() == false){
                return new ArrayList<>();
            }

            return replies.get();
        }
        catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Comment> paginateWithProduct(String productId, int page, int num) {
        try {
            return this.commentRepository.paginateWithProduct(productId, page, num);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
