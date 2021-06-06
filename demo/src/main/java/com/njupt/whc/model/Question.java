package com.njupt.whc.model;

import lombok.Data;

@Data
public class Question {
    private Integer id;
    private String questiontitle;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
}
