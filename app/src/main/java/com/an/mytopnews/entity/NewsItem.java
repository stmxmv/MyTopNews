package com.an.mytopnews.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsItem {

    String title;
    String source;
    String date;
    String imageUrl;

}
