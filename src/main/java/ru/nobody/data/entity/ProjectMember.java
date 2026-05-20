package ru.nobody.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project_member")
public class ProjectMember {

//    @Id
//    @GeneratedValue
//    @UuidGenerator(style = UuidGenerator.Style.AUTO)
//    private UUID id;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

//    @Column(name = "user_id")
//    private UUID userId;
//
//    @Column(name = "project_id")
//    private UUID projectId;
}
