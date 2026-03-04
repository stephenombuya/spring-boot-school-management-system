package com.schoolmanagement.service;

import com.schoolmanagement.dto.request.MessageRequest;
import com.schoolmanagement.dto.response.MessageResponse;
import com.schoolmanagement.dto.response.PagedResponse;
import com.schoolmanagement.exception.AccessDeniedException;
import com.schoolmanagement.exception.ResourceNotFoundException;
import com.schoolmanagement.model.Message;
import com.schoolmanagement.model.User;
import com.schoolmanagement.repository.MessageRepository;
import com.schoolmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageResponse sendMessage(MessageRequest request, String senderEmail) {
        User sender = userRepository.findByEmail(senderEmail).orElseThrow();
        User receiver = userRepository.findById(request.getReceiverId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getReceiverId()));

        Message.MessageBuilder builder = Message.builder()
            .sender(sender).receiver(receiver)
            .subject(request.getSubject())
            .content(request.getContent())
            .relatedCourseId(request.getRelatedCourseId());

        if (request.getParentMessageId() != null) {
            Message parent = messageRepository.findById(request.getParentMessageId())
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", request.getParentMessageId()));
            builder.parentMessage(parent);
        }

        return toResponse(messageRepository.save(builder.build()));
    }

    public PagedResponse<MessageResponse> getInbox(String userEmail, int page, int size) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> result = messageRepository.findInbox(user.getId(), pageable);
        return toPagedResponse(result);
    }

    public PagedResponse<MessageResponse> getSentMessages(String userEmail, int page, int size) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> result = messageRepository.findSent(user.getId(), pageable);
        return toPagedResponse(result);
    }

    public PagedResponse<MessageResponse> getConversation(String userEmail, Long otherUserId, int page, int size) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> result = messageRepository.findConversation(user.getId(), otherUserId, pageable);
        return toPagedResponse(result);
    }

    @Transactional
    public MessageResponse markAsRead(Long messageId, String readerEmail) {
        Message message = findById(messageId);
        User reader = userRepository.findByEmail(readerEmail).orElseThrow();

        if (!message.getReceiver().getId().equals(reader.getId())) {
            throw new AccessDeniedException("You can only mark your own messages as read");
        }

        message.setRead(true);
        message.setReadAt(LocalDateTime.now());
        return toResponse(messageRepository.save(message));
    }

    @Transactional
    public int markAllAsRead(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return messageRepository.markAllAsRead(user.getId());
    }

    @Transactional
    public void deleteMessage(Long messageId, String userEmail) {
        Message message = findById(messageId);
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        if (message.getSender().getId().equals(user.getId())) {
            message.setDeletedBySender(true);
        } else if (message.getReceiver().getId().equals(user.getId())) {
            message.setDeletedByReceiver(true);
        } else {
            throw new AccessDeniedException("You do not have permission to delete this message");
        }

        messageRepository.save(message);
    }

    public long getUnreadCount(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return messageRepository.countUnreadMessages(user.getId());
    }

    private Message findById(Long id) {
        return messageRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Message", "id", id));
    }

    private MessageResponse toResponse(Message m) {
        return MessageResponse.builder()
            .id(m.getId())
            .senderId(m.getSender().getId()).senderName(m.getSender().getFullName())
            .receiverId(m.getReceiver().getId()).receiverName(m.getReceiver().getFullName())
            .subject(m.getSubject()).content(m.getContent())
            .read(m.isRead()).readAt(m.getReadAt())
            .relatedCourseId(m.getRelatedCourseId())
            .parentMessageId(m.getParentMessage() != null ? m.getParentMessage().getId() : null)
            .sentAt(m.getSentAt())
            .build();
    }

    private PagedResponse<MessageResponse> toPagedResponse(Page<Message> page) {
        return PagedResponse.<MessageResponse>builder()
            .content(page.getContent().stream().map(this::toResponse).toList())
            .page(page.getNumber()).size(page.getSize())
            .totalElements(page.getTotalElements()).totalPages(page.getTotalPages())
            .last(page.isLast()).build();
    }
}
