package com.web.web.controller;

import com.common.dto.request.SendMessageDTO;
import com.common.exception.ResourceForbiddenException;
import com.core.jpa.service.MessagePersistenceService;
import com.core.jpa.service.MessageSearchService;
import com.web.web.hateoas.assembler.MessageReceivedResourceAssembler;
import com.web.web.hateoas.assembler.MessageSentResourceAssembler;
import com.web.web.hateoas.resource.MessageReceivedResource;
import com.web.web.hateoas.resource.MessageSentResource;
import com.web.web.security.service.AuthorizationService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/api/v1.0/messages", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(value = "MessageEntity API", description = "Provides a list of methods that manage messages")
public class MessageRestController {

    private final MessagePersistenceService messagePersistenceService;
    private final MessageSearchService messageSearchService;
    private final AuthorizationService authorizationService;
    private final MessageSentResourceAssembler messageSentResourceAssembler;
    private final MessageReceivedResourceAssembler messageReceivedResourceAssembler;

    /**
     * Constructor.
     *
     * @param messagePersistenceService The message persistence service to use
     * @param messageSearchService The message search service to use
     * @param authorizationService The authorization service to use
     * @param messageSentResourceAssembler Assemble message sent resources out of messages
     * @param messageReceivedResourceAssembler Assemble message received resources out of messages
     */
    @Autowired
    public MessageRestController(
            final MessagePersistenceService messagePersistenceService,
            final MessageSearchService messageSearchService,
            final AuthorizationService authorizationService,
            final MessageSentResourceAssembler messageSentResourceAssembler,
            final MessageReceivedResourceAssembler messageReceivedResourceAssembler
    ) {
        this.messagePersistenceService = messagePersistenceService;
        this.messageSearchService = messageSearchService;
        this.authorizationService = authorizationService;
        this.messageSentResourceAssembler = messageSentResourceAssembler;
        this.messageReceivedResourceAssembler = messageReceivedResourceAssembler;
    }

    @ApiOperation(value = "Save message")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the form"),
            @ApiResponse(code = 403, message = "Forbidden command"),
            @ApiResponse(code = 409, message = "Conflict with user ID")
    })
    @PostMapping
    public
    HttpEntity<Boolean> addMessage(
            @ApiParam(value = "MessageEntity", required = true) @RequestBody @Valid final SendMessageDTO sendMessageDTO,
            final UriComponentsBuilder uriComponentsBuilder
    ) {
        log.info("Called with {}", sendMessageDTO);

        this.validUsername(sendMessageDTO.getTo());

        this.messagePersistenceService.createMessage(this.authorizationService.getUserId(), sendMessageDTO);

        UriComponents uriComponents
                = uriComponentsBuilder.path("/profile/{username}").buildAndExpand(sendMessageDTO.getTo());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(true, httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get sent message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No message found or no user found") })
    @GetMapping(value = "/sent/{id}")
    public
    HttpEntity<MessageSentResource> getMessageSent(
            @ApiParam(value = "The message ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        return ResponseEntity.ok()
                .body(this.messageSentResourceAssembler.toResource(this.messageSearchService.getMessageSent(id, this.authorizationService.getUserId())));
    }

    @ApiOperation(value = "Get received message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No message found or no user found") })
    @GetMapping(value = "/received/{id}")
    public
    HttpEntity<MessageReceivedResource> getMessageReceived(
            @ApiParam(value = "The message ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        return ResponseEntity.ok()
                .body(this.messageReceivedResourceAssembler.toResource(this.messageSearchService.getMessageReceived(id, this.authorizationService.getUserId())));
    }

    @ApiOperation(value = "Remove sent message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No message found") })
    @DeleteMapping(value = "/sent/{id}")
    public
    HttpEntity<Boolean> removeMessageSent(
            @ApiParam(value = "The message ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        this.messagePersistenceService.deleteMessageSent(id, this.authorizationService.getUserId());

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Remove received message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No message found") })
    @DeleteMapping(value = "/received/{id}")
    public
    HttpEntity<Boolean> removeMessageReceived(
            @ApiParam(value = "The message ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        this.messagePersistenceService.deleteMessageReceived(id, this.authorizationService.getUserId());

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Get sent messages")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @GetMapping(value = "/sent")
    public
    HttpEntity<List<MessageSentResource>> getMessagesSent(
            @ApiParam(value = "Search for a phrase") @RequestParam(required = false) final String q
    ) {
        log.info("Called with q {}", q);

        return ResponseEntity.ok().body(Optional.ofNullable(q)
                .map(var ->
                        this.messageSearchService
                                .getMessagesSent(this.authorizationService.getUserId(), q)
                                .stream()
                                .map(this.messageSentResourceAssembler::toResource)
                                .collect(Collectors.toList())
                )
                .orElseGet(() ->
                        this.messageSearchService
                                .getMessagesSent(this.authorizationService.getUserId())
                                .stream()
                                .map(this.messageSentResourceAssembler::toResource)
                                .collect(Collectors.toList())
                ));
    }

    @ApiOperation(value = "Get received messages")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @GetMapping(value = "/received")
    public
    HttpEntity<List<MessageReceivedResource>> getMessagesReceived(
            @ApiParam(value = "Search for a phrase") @RequestParam(required = false) final String q
    ) {
        log.info("Called with q {}", q);

        return ResponseEntity.ok().body(Optional.ofNullable(q)
                .map(var ->
                        this.messageSearchService
                                .getMessagesReceived(this.authorizationService.getUserId(), q)
                                .stream()
                                .map(this.messageReceivedResourceAssembler::toResource)
                                .collect(Collectors.toList())
                )
                .orElseGet(() ->
                        this.messageSearchService
                                .getMessagesReceived(this.authorizationService.getUserId())
                                .stream()
                                .map(this.messageReceivedResourceAssembler::toResource)
                                .collect(Collectors.toList())
                ));
    }


    /**
     * This method validates whether the username is not the name of the authorised user.
     *
     * @param username The user's name
     * @throws ResourceForbiddenException if the username is the same as the authorised user's name
     */
    private void validUsername(final String username) {
        if(username.equals(this.authorizationService.getUsername()))
            throw new ResourceForbiddenException("Forbidden command");
    }
}
