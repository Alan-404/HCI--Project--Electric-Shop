package com.hci.electric.dtos.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EditCommentResponse {
    private String message = "";
    private boolean status = false;
    private CommentResponse comment = null;
}
