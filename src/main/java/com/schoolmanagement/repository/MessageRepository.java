package com.schoolmanagement.repository;

import com.schoolmanagement.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.receiver.id = :userId AND m.isDeletedByReceiver = false ORDER BY m.sentAt DESC")
    Page<Message> findInbox(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.sender.id = :userId AND m.isDeletedBySender = false ORDER BY m.sentAt DESC")
    Page<Message> findSent(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiver.id = :userId AND m.isRead = false AND m.isDeletedByReceiver = false")
    long countUnreadMessages(@Param("userId") Long userId);

    @Query("SELECT m FROM Message m WHERE (m.sender.id = :user1 AND m.receiver.id = :user2) OR " +
           "(m.sender.id = :user2 AND m.receiver.id = :user1) ORDER BY m.sentAt ASC")
    Page<Message> findConversation(@Param("user1") Long user1, @Param("user2") Long user2, Pageable pageable);

    @Modifying
    @Query("UPDATE Message m SET m.isRead = true, m.readAt = CURRENT_TIMESTAMP WHERE m.receiver.id = :userId AND m.isRead = false")
    int markAllAsRead(@Param("userId") Long userId);
}
