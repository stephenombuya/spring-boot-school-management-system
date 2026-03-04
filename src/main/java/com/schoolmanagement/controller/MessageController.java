package com.schoolmanagement.controller;

import com.schoolmanagement.dto.request.MessageRequest;
import com.schoolmanagement.dto.response.ApiResponse;
import com.schoolmanagement.dto.response.MessageResponse;
import com.schoolmanagement.dto.response.PagedResponse;
import com.schoolmanagement.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Tag(name = "Communication System")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @Operation(summary = "Send a message to another user")
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(
            @Valid @RequestBody MessageRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(messageService.sendMessage(request, userDetails.getUsername()), "Message sent"));
    }

    @GetMapping("/inbox")
    @Operation(summary = "Get inbox messages")
    public ResponseEntity<ApiResponse<PagedResponse<MessageResponse>>> getInbox(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(messageService.getInbox(userDetails.getUsername(), page, size)));
    }

    @GetMapping("/sent")
    @Operation(summary = "Get sent messages")
    public ResponseEntity<ApiResponse<PagedResponse<MessageResponse>>> getSent(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(messageService.getSentMessages(userDetails.getUsername(), page, size)));
    }

    @GetMapping("/conversation/{userId}")
    @Operation(summary = "Get conversation with a specific user")
    public ResponseEntity<ApiResponse<PagedResponse<MessageResponse>>> getConversation(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return ResponseEntity.ok(ApiResponse.success(
            messageService.getConversation(userDetails.getUsername(), userId, page, size)));
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark a message as read")
    public ResponseEntity<ApiResponse<MessageResponse>> markAsRead(@PathVariable Long id,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(messageService.markAsRead(id, userDetails.getUsername())));
    }

    @PatchMapping("/read-all")
    @Operation(summary = "Mark all inbox messages as read")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> markAllAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        int count = messageService.markAllAsRead(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(Map.of("markedRead", count)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a message")
    public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable Long id,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        messageService.deleteMessage(id, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(null, "Message deleted"));
    }

    @GetMapping("/unread-count")
    @Operation(summary = "Get unread message count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getUnreadCount(@AuthenticationPrincipal UserDetails userDetails) {
        long count = messageService.getUnreadCount(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(Map.of("unreadCount", count)));
    }
}
