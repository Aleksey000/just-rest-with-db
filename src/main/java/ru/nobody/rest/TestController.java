package ru.nobody.rest;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.nobody.data.entity.DbData;
import ru.nobody.data.repository.TestRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TestController {

    @Setter(onMethod = @__({@Autowired}))
    private TestRepository testRepository;


    @PostMapping("/put")
    public Answer putData(@RequestBody String data) {
        DbData dbData = new DbData();
        dbData.setData(data);
        testRepository.save(dbData);
        return Answer.builder().status(RestStatus.builder()
                        .result("OK")
                        .text("Чики-пуки")
                        .build())
                .build();
    }

    @GetMapping("/list")
    public PutDataResult putData(
            @RequestParam(value = "page", defaultValue = "0")
            int page,
            @RequestParam(value = "pageSize", defaultValue = "100")
            int size
    ) {
        List<DbData> dbData = testRepository.justFindAll(PageRequest.of(page, size));

        return PutDataResult.builder()
                .status(RestStatus.builder()
                        .result("OK")
                        .text("Чики-пуки")
                        .build())
                .list(dbData.stream()
                        .map(DbData::getData)
                        .collect(Collectors.toList())
                )
                .page(page)
                .pageSize(size)
                .build();
    }


    @Data
    @Builder
    public static class Answer {
        private RestStatus status;
    }

    @Data
    @Builder
    public static class RestStatus {
        private String result;
        private String text;
    }

    @Data
    @Builder
    public static class PutDataResult {
        private RestStatus status;
        private int page;
        private int pageSize;
        private List<String> list;
    }
}
