package com.example.demo;

import java.util.List;

public class PageResponse {
    public List<DemoEntity> items;
    public String nextPageUrl;

    public PageResponse(List<DemoEntity> items, String nextPageUrl) {
        this.items = items;
        this.nextPageUrl = nextPageUrl;
    }
}