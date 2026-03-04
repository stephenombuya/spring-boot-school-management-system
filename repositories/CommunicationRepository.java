package com.schoolmanagement.repositories;

import com.schoolmanagement.model.Communication;
import com.schoolmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Long> {
    List<Communication> findBySender(User sender);
    List<Communication> findByRecipient(User recipient);
    List<Communication> findBySenderAndRecipient(User sender, User recipient);
    List<Communication> findByRecipientAndStatus(User recipient, Communication.MessageStatus status);
}
