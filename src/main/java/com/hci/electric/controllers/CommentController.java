package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.comment.CommentInfo;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Comment;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final AccountService accountService;
    private Auth auth;

    public CommentController(CommentService commentService, AccountService accountService){
        this.commentService = commentService;
        this.accountService = accountService;
        this.auth = new Auth(this.accountService);
    }

    @PostMapping("/api")
    public ResponseEntity<Comment> saveComment(HttpServletRequest httpServletRequest, @RequestBody Comment comment){
        String token = httpServletRequest.getHeader("Authorization");

        Account account = this.auth.checkToken(token);

        if (account == null){
            return ResponseEntity.status(400).body(null);
        }

        comment.setUserId(account.getUserId());

        Comment savedComment = this.commentService.save(comment);
        if (savedComment == null){
            return ResponseEntity.status(500).body(null);
        }

        return ResponseEntity.status(200).body(savedComment);
    }
    

    @GetMapping("/api")
    public ResponseEntity<List<CommentInfo>> getCommentsOfProduct(@RequestParam("id") String id){
        List<Comment> comments = this.commentService.getCommentsByProduct(id);
        List<CommentInfo> items = new ArrayList<>();

        for (Comment comment : comments) {
            CommentInfo item = new CommentInfo(comment, this.commentService.getRepliesOfComment(comment.getId()).size());
            items.add(item);
        }

        return ResponseEntity.status(200).body(items);
    }

}
