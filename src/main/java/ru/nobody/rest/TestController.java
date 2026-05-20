package ru.nobody.rest;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.nobody.data.entity.DbData;
import ru.nobody.data.repository.TestRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

// TODO логи
@Slf4j
@RestController
@RequestMapping("/v1/data")
public class TestController {

    @Setter(onMethod = @__({@Autowired}))
    private TestRepository testRepository;


    @PostMapping("/put")
    public Answer putData(@RequestBody String data) {
        DbData dbData = new DbData();
        dbData.setData(data);
        DbData save = testRepository.save(dbData);
        return Answer.builder().status(RestStatus.builder()
                        .result("OK")
                        .text("Чики-пуки")
                        .build())
                .id(save.getId())
                .build();
    }

    @GetMapping("/get")
    public GetDataResult get(
            @RequestParam(value = "id", required = false)
            String id
    ) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(id);
        } catch (Exception ex) {
            // ignore
        }
        log.info("id {}", uuid);


        Optional<DbData> dataOptional = uuid == null ? Optional.empty() : testRepository.findById(uuid);

        UUID finalUuid = uuid;
        testRepository.findAll().forEach(item -> {
            if (item.getId().equals(finalUuid)) {
                log.info("FIND!!");
            }
        });

        return GetDataResult.builder()
                .status(RestStatus.builder()
                        .result(dataOptional.isPresent() ? "OK" : "NOT_FOUNDED")
                        .text(dataOptional.isPresent() ? "Чики-пуки" : "Я ничё не нашёл")
                        .build())
                .item(dataOptional.map(DbData::getData).orElse(null))
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
        private UUID id;
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

    @Data
    @Builder
    public static class GetDataResult {
        private RestStatus status;
        private String item;
    }


}
