package com.schoolmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String subject;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "is_deleted_by_sender")
    @Builder.Default
    private boolean isDeletedBySender = false;

    @Column(name = "is_deleted_by_receiver")
    @Builder.Default
    private boolean isDeletedByReceiver = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_message_id")
    private Message parentMessage;

    @Column(name = "related_course_id")
    private Long relatedCourseId;

    @CreatedDate
    @Column(name = "sent_at", updatable = false)
    private LocalDateTime sentAt;
}
