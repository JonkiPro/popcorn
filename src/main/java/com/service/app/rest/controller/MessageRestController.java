package com.service.app.rest.controller;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.rest.request.SendMessageDTO;
import com.service.app.rest.response.ReceivedMessageDTO;
import com.service.app.rest.response.ReceivedMessageInfoDTO;
import com.service.app.rest.response.SentMessageDTO;
import com.service.app.rest.response.SentMessageInfoDTO;
import com.service.app.entity.Message;
import com.service.app.exception.AccessToMessageForbiddenException;
import com.service.app.service.AuthorizationService;
import com.service.app.service.MessageService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "Message API", description = "Provides a list of methods that manage messages")
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

    @ApiOperation(value = "Save message", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Incorrect data in the form") })
    @PostMapping(value = "/sendMessage")
    @ResponseStatus(HttpStatus.CREATED)
    public
    HttpEntity<Boolean> sendMessage(
            @ApiParam(value = "Message", required = true) @RequestBody @Valid SendMessageDTO sendMessageDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        messageService.saveMessage(converterSendMessageDTOToMessage.convert(sendMessageDTO));

        UriComponents uriComponents
                = uriComponentsBuilder.path("/profile/{username}").buildAndExpand(sendMessageDTO.getTo());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(true, httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get all sent messages by user ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No messages found") })
    @GetMapping(value = "/getSentMessages")
    public
    HttpEntity<List<SentMessageInfoDTO>> getSentMessageInfoDTO() {
        List<Message> messageList = messageService.findBySender(authorizationService.getUserId());

        return !messageList.isEmpty() ?
                ResponseEntity.ok().body(converterMessageToSentMessageInfoDTO.convertAll(messageList)) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Get all received messages by user ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No messages found") })
    @GetMapping(value = "/getReceivedMessages")
    public
    HttpEntity<List<ReceivedMessageInfoDTO>> getReceivedMessageInfoDTO() {
        List<Message> messageList = messageService.findByRecipient(authorizationService.getUserId());

        return !messageList.isEmpty() ?
                ResponseEntity.ok().body(converterMessageToReceivedMessageInfoDTO.convertAll(messageList)) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Remove sent message by ID")
    @ApiResponses(value = { @ApiResponse(code = 403, message = "No messages found or you have not accessed") })
    @DeleteMapping(value = "/removeSentMessage")
    public
    HttpEntity<Boolean> removeSentMessage(
            @ApiParam(value = "The message ID", required = true) @RequestParam Long id
    ) {
        return messageService.findBySender(authorizationService.getUserId()).stream().anyMatch(message -> message.getId().equals(id)) ?
                ResponseEntity.ok().body(messageService.removeSentMessage(id)) : new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Remove received message by ID")
    @ApiResponses(value = { @ApiResponse(code = 403, message = "No messages found or you have not accessed") })
    @DeleteMapping(value = "/removeReceivedMessage")
    public
    HttpEntity<Boolean> removeReceivedMessage(
            @ApiParam(value = "The message ID", required = true) @RequestParam Long id
    ) {
        return messageService.findByRecipient(authorizationService.getUserId()).stream().anyMatch(message -> message.getId().equals(id)) ?
                ResponseEntity.ok().body(messageService.removeReceivedMessage(id)) : new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    }

    @ApiOperation(value = "Search sent messages by phrase")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No messages found") })
    @GetMapping(value = "/searchSentMessages")
    public
    HttpEntity<List<SentMessageInfoDTO>> searchSentMessage(
            @ApiParam(value = "Search for a phrase", required = true) @RequestParam String q
    ) {
        List<Message> messageList = messageService.findSentMessagesByContaining(authorizationService.getUserId(), q);

        return !messageList.isEmpty() ?
                ResponseEntity.ok().body(converterMessageToSentMessageInfoDTO.convertAll(messageList)) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Search received messages by phrase")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No messages found") })
    @GetMapping(value = "/searchReceivedMessages")
    public
    HttpEntity<List<ReceivedMessageInfoDTO>> searchReceivedMessage(
            @ApiParam(value = "Search for a phrase", required = true) @RequestParam String q
    ) {
        List<Message> messageList = messageService.findReceivedMessagesByContaining(authorizationService.getUserId(), q);

        return !messageList.isEmpty() ?
                ResponseEntity.ok().body(converterMessageToReceivedMessageInfoDTO.convertAll(messageList)) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Get sent message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No messages found") })
    @GetMapping(value = "/getSentMessage")
    public
    HttpEntity<SentMessageDTO> getSentMessageDTO(
            @ApiParam(value = "The message ID", required = true) @RequestParam Long id
    ) {
        Optional<Message> messageOptional = messageService.findByIdAndSender(id, authorizationService.getUserId());

        return messageOptional
                .map(message -> ResponseEntity.ok().body(converterMessageToSentMessageDTO.convert(message)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Get received message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No messages found") })
    @GetMapping(value = "/getReceivedMessage")
    public
    HttpEntity<ReceivedMessageDTO> getReceivedMessageDTO(
            @ApiParam(value = "The message ID", required = true) @RequestParam Long id
    ) {
        Optional<Message> messageOptional = messageService.findByIdAndRecipient(id, authorizationService.getUserId());

        return messageOptional
                .map(message -> ResponseEntity.ok().body(converterMessageToReceivedMessageDTO.convert(message)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Set the date to read the message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No messages found") })
    @PutMapping(value = "/setDateOfRead")
    @SuppressWarnings("ConstantConditions")
    public
    HttpEntity<Date> setDateOfRead(
            @ApiParam(value = "The message ID", required = true) @RequestParam Long id
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

    /**
     * This method checks to see if the message belongs to the user.
     * @param messageId The message ID.
     */
    private void validAccessToMessage(Long messageId) {
        List<Message> messageList = messageService.findBySender(authorizationService.getUserId());
        messageList.addAll(messageService.findByRecipient(authorizationService.getUserId()));

        messageList.stream()
                .filter(v -> messageId.equals(v.getId()))
                .findAny()
                .orElseThrow(AccessToMessageForbiddenException::new);
    }
}
