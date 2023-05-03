package com.hci.electric.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hci.electric.dtos.comment.CommentInfo;
import com.hci.electric.dtos.comment.CommentPaginateResponse;
import com.hci.electric.dtos.comment.CommentResponse;
import com.hci.electric.dtos.comment.CommentUser;
import com.hci.electric.middlewares.Auth;
import com.hci.electric.models.Account;
import com.hci.electric.models.Comment;
import com.hci.electric.models.User;
import com.hci.electric.services.AccountService;
import com.hci.electric.services.CommentService;
import com.hci.electric.services.UserService;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final AccountService accountService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private Auth auth;

    public CommentController(
        CommentService commentService,
        AccountService accountService,
        UserService userService) {
        this.commentService = commentService;
        this.accountService = accountService;
        this.modelMapper = new ModelMapper();
        this.userService = userService;
        this.auth = new Auth(this.accountService);
    }

    @PostMapping("/api")
    public ResponseEntity<CommentResponse> saveComment(HttpServletRequest httpServletRequest, @RequestBody Comment comment){
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

        CommentResponse response = this.modelMapper.map(savedComment, CommentResponse.class);
        User user = this.userService.getById(savedComment.getUserId());
        Account userAccount = this.accountService.getByUserId(user.getId());
        CommentUser commentUser = this.modelMapper.map(user, CommentUser.class);

        commentUser.setRole(userAccount.getRole());
        response.setUser(commentUser);

        return ResponseEntity.status(200).body(response);
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

    @GetMapping("/product/{id}")
    public ResponseEntity<CommentPaginateResponse> getCommentsByProduct(
        @PathVariable("id") String productId,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer num) {

        int totalItems = this.commentService.getCommentsByProduct(productId).size();
        int totalPage = 0;

        if (totalItems > 0) {
            if (page == null) {
                page = 0;
            }

            if (num == null) {
                num = totalItems;
            }

            totalPage = totalItems / num;

            if (totalItems % num != 0) {
                totalPage++;
            }
        }

        List<Comment> comments = this.commentService.paginateWithProduct(productId, page, num);
        List<CommentResponse> responses = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponse response = this.modelMapper.map(comment, CommentResponse.class);
            User user = this.userService.getById(comment.getUserId());
            Account account = this.accountService.getByUserId(user.getId());
            CommentUser commentUser = this.modelMapper.map(user, CommentUser.class);

            List<CommentResponse> replyResponses = new ArrayList<>();

            
            List<Comment> replies = this.commentService.getRepliesOfComment(comment.getId());
            System.out.println("Replies Size.................: " + replies.size());
            for (Comment reply : replies) {
                CommentResponse replyResponse = this.modelMapper.map(reply, CommentResponse.class);
                User replyUser = this.userService.getById(reply.getUserId());
                Account replyAccount = this.accountService.getByUserId(replyUser.getId());
                CommentUser replyCommentUser = this.modelMapper.map(replyUser, CommentUser.class);

                replyCommentUser.setRole(replyAccount.getRole());
                replyResponse.setUser(replyCommentUser);

                replyResponses.add(replyResponse);
            }
            
            
            commentUser.setRole(account.getRole());
            response.setUser(commentUser);
            response.setReplies(replyResponses);
            responses.add(response);
        }

        CommentPaginateResponse response = new CommentPaginateResponse(responses, totalPage, totalItems, page, num);

        return ResponseEntity.status(200).body(response);
    }

}
