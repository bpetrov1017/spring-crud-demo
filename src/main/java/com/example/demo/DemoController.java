package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/entities")
public class DemoController {

    private final DemoRepository repo;
    private static final int PAGE_SIZE = 20;

    public DemoController(DemoRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/old")
    public PageResponse getOld(
            @RequestParam(defaultValue = "0") int page
    ) {
        int pageSize = 20;

        List<DemoEntity> all = repo.findAllSorted();

        List<DemoEntity> items = all.stream()
                .skip((long) page * pageSize)
                .limit(pageSize)
                .toList();

        boolean hasNext = all.size() > (page + 1) * pageSize;

        String nextPageUrl = hasNext
                ? "https://glorious-goldfish-r7grvwqj6wr9c54p4-8080.app.github.dev/entities/old?page=" + (page + 1)
                : null;

        return new PageResponse(items, nextPageUrl);
    }

    // CREATE
    @PostMapping
    public DemoEntity create(@RequestBody DemoEntity e) {
        return repo.save(e);
    }

    @GetMapping("/new")
    public PageResponse getPage(
            @RequestParam(required = false) String cursor
    ) {
        int pageSize = 20;

        Instant cursorTime = (cursor == null)
                ? Instant.now()
                : Instant.parse(cursor);

        List<DemoEntity> items = repo.findNextPage(cursorTime);

        List<DemoEntity> page = items.stream()
                .limit(pageSize)
                .toList();

        DemoEntity last = page.isEmpty() ? null : page.get(page.size() - 1);

        String nextPageUrl = null;

        if (page.size() == pageSize && last != null) {
            nextPageUrl =
                    "https://glorious-goldfish-r7grvwqj6wr9c54p4-8080.app.github.dev/entities/new?cursor="
                            + last.getModifiedAt();
        }

        return new PageResponse(page, nextPageUrl);
    }
}