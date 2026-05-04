package ru.nobody.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity(name = "DbData")
@Table(name = "just_data")
@Data
public class DbData {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(columnDefinition = "text")
    private String data;

    /**
     * Автогененируемый столбец, дата создания
     */
    @CreationTimestamp
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    /**
     * Автогененируемый столбец, дата обновления
     */
    @UpdateTimestamp
    @Column(name = "modified_at")
    private ZonedDateTime modifiedAt;
}
