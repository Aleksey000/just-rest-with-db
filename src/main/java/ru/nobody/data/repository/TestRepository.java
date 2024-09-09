package ru.nobody.data.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.nobody.data.entity.DbData;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestRepository extends CrudRepository<DbData, UUID> {

    @Query("select d from DbData as d")
    List<DbData> justFindAll(Pageable pageable);
}
