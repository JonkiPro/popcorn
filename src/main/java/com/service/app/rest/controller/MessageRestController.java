package com.service.app.rest.controller;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.exception.ResourceForbiddenException;
import com.service.app.rest.request.SendMessageDTO;
import com.service.app.rest.response.*;
import com.service.app.entity.Message;
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
import java.util.List;
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/api/v1.0/messages", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "Save message")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the form"),
            @ApiResponse(code = 403, message = "Authorised user")
    })
    @PostMapping
    public
    HttpEntity<Boolean> addMessage(
            @ApiParam(value = "Message", required = true) @RequestBody @Valid SendMessageDTO sendMessageDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        this.validUsername(sendMessageDTO.getTo());

        messageService.saveMessage(converterSendMessageDTOToMessage.convert(sendMessageDTO));

        UriComponents uriComponents
                = uriComponentsBuilder.path("/profile/{username}").buildAndExpand(sendMessageDTO.getTo());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(true, httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get sent message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No message found") })
    @GetMapping(value = "/sent/{id}")
    public
    HttpEntity<SentMessageDTO> getSentMessage(
            @ApiParam(value = "The message ID", required = true) @PathVariable Long id
    ) {
        return messageService.findByIdAndSender(id, authorizationService.getUser())
                .map(message -> ResponseEntity.ok().body(converterMessageToSentMessageDTO.convert(message)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Get received message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No message found") })
    @GetMapping(value = "/received/{id}")
    public
    HttpEntity<ReceivedMessageDTO> getReceivedMessage(
            @ApiParam(value = "The message ID", required = true) @PathVariable Long id
    ) {
        return messageService.findByIdAndRecipient(id, authorizationService.getUser())
                .map(message -> ResponseEntity.ok().body(converterMessageToReceivedMessageDTO.convert(message)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Remove sent message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No message found") })
    @DeleteMapping(value = "/sent/{id}")
    public
    HttpEntity<Boolean> removeSentMessage(
            @ApiParam(value = "The message ID", required = true) @PathVariable Long id
    ) {
        return messageService.findBySender(authorizationService.getUser()).stream().anyMatch(message -> message.getId().equals(id)) ?
                ResponseEntity.ok().body(messageService.removeSentMessage(id)) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Remove received message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No message found") })
    @DeleteMapping(value = "/received/{id}")
    public
    HttpEntity<Boolean> removeReceivedMessage(
            @ApiParam(value = "The message ID", required = true) @PathVariable Long id
    ) {
        return messageService.findByRecipient(authorizationService.getUser()).stream().anyMatch(message -> message.getId().equals(id)) ?
                ResponseEntity.ok().body(messageService.removeReceivedMessage(id)) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Get sent messages")
    @GetMapping(value = "/sent")
    public
    HttpEntity<List<SentMessageInfoDTO>> getSentMessages(
            @ApiParam(value = "Search for a phrase") @RequestParam(required = false) String q
    ) {
        return Optional
                .ofNullable(q)
                .map(var -> {
                    List<Message> messageList
                            = messageService.findSentMessagesByContaining(authorizationService.getUser(), q);

                    return ResponseEntity
                            .ok()
                            .body(converterMessageToSentMessageInfoDTO.convertAll(messageList));
                })
                .orElseGet(() -> {
                    List<Message> messageList
                            = messageService.findBySender(authorizationService.getUser());

                    return ResponseEntity
                            .ok()
                            .body(converterMessageToSentMessageInfoDTO.convertAll(messageList));
                });
    }

    @ApiOperation(value = "Get received messages")
    @GetMapping(value = "/received")
    public
    HttpEntity<List<ReceivedMessageInfoDTO>> getReceivedMessages(
            @ApiParam(value = "Search for a phrase") @RequestParam(required = false) String q
    ) {
        return Optional
                .ofNullable(q)
                .map(var -> {
                    List<Message> messageList
                            = messageService.findReceivedMessagesByContaining(authorizationService.getUser(), q);

                    return ResponseEntity
                            .ok()
                            .body(converterMessageToReceivedMessageInfoDTO.convertAll(messageList));
                })
                .orElseGet(() -> {
                    List<Message> messageList
                            = messageService.findByRecipient(authorizationService.getUser());

                    return ResponseEntity
                            .ok()
                            .body(converterMessageToReceivedMessageInfoDTO.convertAll(messageList));
                });
    }


    /**
     * This method validates whether the username is not the name of the authorised user.
     * @param username The user's name.
     * @throws ResourceForbiddenException if the username is the same as the authorised user's name
     */
    private void validUsername(String username) {
        if(username.equals(authorizationService.getUsername()))
            throw new ResourceForbiddenException("You cannot provide an authorised user name");
    }
}
