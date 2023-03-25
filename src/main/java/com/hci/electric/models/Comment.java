package com.hci.electric.models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMMENT")
public class Comment {
    @Id
    private String id;
    private String userId;
    private String productId;
    private String content;
    private String reply;
    private Timestamp createdAt;
    private Timestamp modifiedAt;   
}
