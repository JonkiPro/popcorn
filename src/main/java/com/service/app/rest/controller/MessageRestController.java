package com.service.app.rest.controller;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.dto.in.SendMessageDTO;
import com.service.app.dto.out.ReceivedMessageDTO;
import com.service.app.dto.out.ReceivedMessageInfoDTO;
import com.service.app.dto.out.SentMessageDTO;
import com.service.app.dto.out.SentMessageInfoDTO;
import com.service.app.entity.Message;
import com.service.app.exception.AccessToMessageForbiddenException;
import com.service.app.service.AuthorizationService;
import com.service.app.service.MessageService;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiStage;
import org.jsondoc.core.pojo.ApiVerb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(name = "Message API", description = "Provides a list of methods that manage messages", group = "Message", stage = ApiStage.BETA)
public class MessageRestController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private UnidirectionalConverter<SendMessageDTO, Message> converterSendMessageDTOToMessage;
    @Autowired
    private UnidirectionalConverter<Message, SentMessageInfoDTO> converterMessageToSentMessageInfoDTO;
    @Autowired
    private UnidirectionalConverter<Message, SentMessageDTO> converterMessageToSentMessageDTO;
    @Autowired
    private UnidirectionalConverter<Message, ReceivedMessageInfoDTO> converterMessageToReceivedMessageInfoDTO;
    @Autowired
    private UnidirectionalConverter<Message, ReceivedMessageDTO> converterMessageToReceivedMessageDTO;

    @ApiMethod(description = "Save message", responsestatuscode = "201")
    @ApiErrors(apierrors = { @ApiError(code = "400", description = "Incorrect data in the form") })
    @PostMapping(value = "/sendMessage")
    @ResponseStatus(HttpStatus.CREATED)
    public @ApiResponseObject
    HttpEntity<Boolean> sendMessage(
            @ApiBodyObject @RequestBody @Valid SendMessageDTO sendMessageDTO
    ) {
        messageService.saveMessage(converterSendMessageDTOToMessage.convert(sendMessageDTO));

        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @ApiMethod(description = "Get all sent messages by user ID")
    @ApiErrors(apierrors = { @ApiError(code = "404", description = "No messages found") })
    @GetMapping(value = "/getSentMessages")
    public @ApiResponseObject
    HttpEntity<List<SentMessageInfoDTO>> getSentMessageInfoDTO() {
        List<Message> messageList = messageService.findBySender(authorizationService.getUserId());

        return !messageList.isEmpty() ?
                ResponseEntity.ok().body(converterMessageToSentMessageInfoDTO.convertAll(messageList)) : ResponseEntity.notFound().build();
    }

    @ApiMethod(description = "Get all received messages by user ID")
    @ApiErrors(apierrors = { @ApiError(code = "404", description = "No messages found") })
    @GetMapping(value = "/getReceivedMessages")
    public @ApiResponseObject
    HttpEntity<List<ReceivedMessageInfoDTO>> getReceivedMessageInfoDTO() {
        List<Message> messageList = messageService.findByRecipient(authorizationService.getUserId());

        return !messageList.isEmpty() ?
                ResponseEntity.ok().body(converterMessageToReceivedMessageInfoDTO.convertAll(messageList)) : ResponseEntity.notFound().build();
    }

    @ApiMethod(description = "Remove sent message by ID", verb = ApiVerb.DELETE)
    @ApiErrors(apierrors = { @ApiError(code = "403", description = "No messages found or you have not accessed") })
    @DeleteMapping(value = "/removeSentMessage")
    public @ApiResponseObject
    HttpEntity<Boolean> removeSentMessage(
            @ApiQueryParam(description = "The message ID") @RequestParam Long id
    ) {
        return messageService.findBySender(authorizationService.getUserId()).stream().anyMatch(message -> message.getId().equals(id)) ?
                ResponseEntity.ok().body(messageService.removeSentMessage(id)) : new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    }

    @ApiMethod(description = "Remove received message by ID", verb = ApiVerb.DELETE)
    @ApiErrors(apierrors = { @ApiError(code = "403", description = "No messages found or you have not accessed") })
    @DeleteMapping(value = "/removeReceivedMessage")
    public @ApiResponseObject
    HttpEntity<Boolean> removeReceivedMessage(
            @ApiQueryParam(description = "The message ID") @RequestParam Long id
    ) {
        return messageService.findByRecipient(authorizationService.getUserId()).stream().anyMatch(message -> message.getId().equals(id)) ?
                ResponseEntity.ok().body(messageService.removeReceivedMessage(id)) : new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    }

    @ApiMethod(description = "Search sent messages by phrase")
    @ApiErrors(apierrors = { @ApiError(code = "404", description = "No messages found") })
    @GetMapping(value = "/searchSentMessages")
    public @ApiResponseObject
    HttpEntity<List<SentMessageInfoDTO>> searchSentMessage(
            @ApiQueryParam(description = "Search for a phrase") @RequestParam String q
    ) {
        List<Message> messageList = messageService.findSentMessagesByContaining(authorizationService.getUserId(), q);

        return !messageList.isEmpty() ?
                ResponseEntity.ok().body(converterMessageToSentMessageInfoDTO.convertAll(messageList)) : ResponseEntity.notFound().build();
    }

    @ApiMethod(description = "Search received messages by phrase")
    @ApiErrors(apierrors = { @ApiError(code = "404", description = "No messages found") })
    @GetMapping(value = "/searchReceivedMessages")
    public @ApiResponseObject
    HttpEntity<List<ReceivedMessageInfoDTO>> searchReceivedMessage(
            @ApiQueryParam(description = "Search for a phrase") @RequestParam String q
    ) {
        List<Message> messageList = messageService.findReceivedMessagesByContaining(authorizationService.getUserId(), q);

        return !messageList.isEmpty() ?
                ResponseEntity.ok().body(converterMessageToReceivedMessageInfoDTO.convertAll(messageList)) : ResponseEntity.notFound().build();
    }

    @ApiMethod(description = "Get sent message by ID")
    @ApiErrors(apierrors = { @ApiError(code = "404", description = "No messages found") })
    @GetMapping(value = "/getSentMessage")
    public @ApiResponseObject
    HttpEntity<SentMessageDTO> getSentMessageDTO(
            @ApiQueryParam(description = "The message ID") @RequestParam Long id
    ) {
        Optional<Message> messageOptional = messageService.findByIdAndSender(id, authorizationService.getUserId());

        return messageOptional
                .map(message -> ResponseEntity.ok().body(converterMessageToSentMessageDTO.convert(message)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiMethod(description = "Get received message by ID")
    @ApiErrors(apierrors = { @ApiError(code = "404", description = "No messages found") })
    @GetMapping(value = "/getReceivedMessage")
    public @ApiResponseObject
    HttpEntity<ReceivedMessageDTO> getReceivedMessageDTO(
            @ApiQueryParam(description = "The message ID") @RequestParam Long id
    ) {
        Optional<Message> messageOptional = messageService.findByIdAndRecipient(id, authorizationService.getUserId());

        return messageOptional
                .map(message -> ResponseEntity.ok().body(converterMessageToReceivedMessageDTO.convert(message)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiMethod(description = "Set the date to read the message by ID", verb = ApiVerb.PUT)
    @PutMapping(value = "/setDateOfRead")
    @SuppressWarnings("ConstantConditions")
    public @ApiResponseObject
    HttpEntity<Date> setDateOfRead(
            @ApiQueryParam(description = "The message ID") @RequestParam Long id
    ) {
        this.validAccessToMessage(id);

        Optional<Message> messageOptional = messageService.findByIdAndRecipient(id, authorizationService.getUserId());

        Message message;

        try {
            message = messageOptional.get();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

        message.setDateOfRead(new Date());

        messageService.saveMessage(message);

        return ResponseEntity.ok(message.getDateOfRead());
    }

    private void validAccessToMessage(Long messageId) {
        List<Message> messageList = messageService.findBySender(authorizationService.getUserId());
        messageList.addAll(messageService.findByRecipient(authorizationService.getUserId()));

        messageList.stream()
                .filter(v -> messageId.equals(v.getId()))
                .findAny()
                .orElseThrow(AccessToMessageForbiddenException::new);
    }
}
