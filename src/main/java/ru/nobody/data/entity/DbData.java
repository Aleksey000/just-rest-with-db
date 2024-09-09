package ru.nobody.data.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity(name = "DbData")
@Data
public class DbData {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(columnDefinition = "text")
    private String data;

    /**
     * Автогененируемый столбец, дата создания
     */
    @CreationTimestamp
    @Column
    private ZonedDateTime createdAt;

    /**
     * Автогененируемый столбец, дата обновления
     */
    @UpdateTimestamp
    @Column
    private ZonedDateTime modifiedAt;
}
