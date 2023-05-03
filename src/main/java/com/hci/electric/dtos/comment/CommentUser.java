package com.hci.electric.dtos.comment;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUser {
    private String id;
    private String firstName;
    private String lastName;
    private String avatar;
    private String role;
}
