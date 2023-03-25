package com.hci.electric.dtos.comment;

import com.hci.electric.models.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentInfo {
    private Comment comment;
    private int numReplies;
}
