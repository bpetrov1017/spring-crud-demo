package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;
import java.util.Base64;

@RestController
@RequestMapping("/entities")
public class DemoController {

    private final DemoRepository repo;
    private static final int PAGE_SIZE = 5;

    public DemoController(DemoRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public DemoEntity create(@RequestBody DemoEntity e) {
        return repo.save(e);
    }

    @GetMapping("/old")
    public PageResponse getOld(@RequestParam(defaultValue = "0") int page) {
        List<DemoEntity> all = repo.findAllSorted();
        List<DemoEntity> items = all.stream() .skip((long)page * PAGE_SIZE) .limit(PAGE_SIZE).toList();

        boolean hasNext = all.size() > (page + 1) * PAGE_SIZE;

        String nextPageUrl = hasNext ? "https://glorious-goldfish-r7grvwqj6wr9c54p4-8080.app.github.dev/entities/old?page=" + (page + 1) : null;

        return new PageResponse(items, nextPageUrl);
    }

    @GetMapping("/new")
     public PageResponse getPage(@RequestParam(required = false) String cursor) {
        Instant cursorTime;
        String cursorId;

        if (cursor == null) {
            cursorTime = Instant.now();
            cursorId = "~~~~~";
        } else {
            try {
                String decoded = new String(Base64.getUrlDecoder().decode(cursor));
                String[] parts = decoded.split("\\|");

                if (parts.length < 2) {
                    throw new IllegalArgumentException("Invalid cursor format");
                }

                cursorTime = Instant.parse(parts[0]);
                cursorId = parts[1];
            } catch (Exception e) {
                String[] parts = cursor.split("\\|");
                cursorTime = Instant.parse(parts[0]);
                cursorId = parts[1];
            }
        }

        List<DemoEntity> items = repo.findNextPage(cursorTime, cursorId);
        List<DemoEntity> page = items.stream().limit(PAGE_SIZE).toList();
        DemoEntity last = page.isEmpty() ? null : page.get(page.size() - 1);
        String nextPageUrl = null;

        if (page.size() == PAGE_SIZE && last != null) {
            String nextCursor = last.getModifiedAt() + "|" + last.getId();
            String nextCursorBase64 = Base64.getUrlEncoder().encodeToString((last.getModifiedAt() + "|" + last.getId()).getBytes());
            nextPageUrl = "https://glorious-goldfish-r7grvwqj6wr9c54p4-8080.app.github.dev/entities/new?cursor=" + nextCursorBase64;
        }

        return new PageResponse(page, nextPageUrl);
    }
}