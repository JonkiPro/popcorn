package com.web.web.controller;

import com.common.dto.error.ValidationErrorDTO;
import com.common.dto.request.SendMessageDTO;
import com.common.exception.FormBadRequestException;
import com.common.exception.ResourceForbiddenException;
import com.core.service.MessagePersistenceService;
import com.core.service.MessageSearchService;
import com.core.service.UserSearchService;
import com.core.properties.BundleProperties;
import com.web.web.hateoas.assembler.MessageReceivedResourceAssembler;
import com.web.web.hateoas.assembler.MessageSentResourceAssembler;
import com.web.web.hateoas.resource.MessageReceivedResource;
import com.web.web.hateoas.resource.MessageSentResource;
import com.web.web.security.service.AuthorizationService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/api/v1.0/messages")
@Slf4j
@Api(value = "Message API", description = "Provides a list of methods that manage messages")
public class MessageRestController {

    private final ResourceBundle bundle = ResourceBundle.getBundle(BundleProperties.VALIDATION_MESSAGES.getSource(), LocaleContextHolder.getLocale());
    private final MessagePersistenceService messagePersistenceService;
    private final MessageSearchService messageSearchService;
    private final UserSearchService userSearchService;
    private final AuthorizationService authorizationService;
    private final MessageSentResourceAssembler messageSentResourceAssembler;
    private final MessageReceivedResourceAssembler messageReceivedResourceAssembler;

    /**
     * Constructor - initialize all required dependencies.
     *
     * @param messagePersistenceService The message persistence service to use
     * @param messageSearchService The message search service to use
     * @param userSearchService The user search service to use
     * @param authorizationService The authorization service to use
     * @param messageSentResourceAssembler Assemble message sent resources out of messages
     * @param messageReceivedResourceAssembler Assemble message received resources out of messages
     */
    @Autowired
    public MessageRestController(
            final MessagePersistenceService messagePersistenceService,
            final MessageSearchService messageSearchService,
            final UserSearchService userSearchService,
            final AuthorizationService authorizationService,
            final MessageSentResourceAssembler messageSentResourceAssembler,
            final MessageReceivedResourceAssembler messageReceivedResourceAssembler
    ) {
        this.messagePersistenceService = messagePersistenceService;
        this.messageSearchService = messageSearchService;
        this.userSearchService = userSearchService;
        this.authorizationService = authorizationService;
        this.messageSentResourceAssembler = messageSentResourceAssembler;
        this.messageReceivedResourceAssembler = messageReceivedResourceAssembler;
    }

    @ApiOperation(value = "Create a message")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the form"),
            @ApiResponse(code = 403, message = "Forbidden command"),
            @ApiResponse(code = 409, message = "Conflict with user ID")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createMessage(
            @ApiParam(value = "New message", required = true)
            @RequestBody @Valid final SendMessageDTO sendMessageDTO,
            final UriComponentsBuilder uriComponentsBuilder
    ) {
        log.info("Called with {}", sendMessageDTO);

        this.validUsername(sendMessageDTO.getTo());

        this.messagePersistenceService.createMessage(this.authorizationService.getUserId(), sendMessageDTO);

        final UriComponents uriComponents
                = uriComponentsBuilder.path("/profile/{username}").buildAndExpand(sendMessageDTO.getTo());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponents.toUri());

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get sent message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No message found or no user found") })
    @GetMapping(value = "/sent/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    MessageSentResource getMessageSent(
            @ApiParam(value = "The message ID", required = true)
            @PathVariable("id") final String id
    ) {
        log.info("Called with id {}", id);

        return this.messageSentResourceAssembler.toResource(this.messageSearchService.getMessageSent(id, this.authorizationService.getUserId()));
    }

    @ApiOperation(value = "Get received message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No message found or no user found") })
    @GetMapping(value = "/received/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    MessageReceivedResource getMessageReceived(
            @ApiParam(value = "The message ID", required = true)
            @PathVariable("id") final String id
    ) {
        log.info("Called with id {}", id);

        return this.messageReceivedResourceAssembler.toResource(this.messageSearchService.getMessageReceived(id, this.authorizationService.getUserId()));
    }

    @ApiOperation(value = "Delete sent message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No message found") })
    @DeleteMapping(value = "/sent/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void deleteMessageSent(
            @ApiParam(value = "The message ID", required = true)
            @PathVariable("id") final String id
    ) {
        log.info("Called with id {}", id);

        this.messagePersistenceService.deleteMessageSent(id, this.authorizationService.getUserId());
    }

    @ApiOperation(value = "Delete received message by ID")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No message found") })
    @DeleteMapping(value = "/received/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void deleteMessageReceived(
            @ApiParam(value = "The message ID", required = true)
            @PathVariable("id") final String id
    ) {
        log.info("Called with id {}", id);

        this.messagePersistenceService.deleteMessageReceived(id, this.authorizationService.getUserId());
    }

    @ApiOperation(value = "Get sent messages")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @GetMapping(value = "/sent", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    List<MessageSentResource> getMessagesSent(
            @ApiParam(value = "Search for a phrase")
            @RequestParam(value = "q", required = false) final String q
    ) {
        log.info("Called with q {}", q);

        return this.messageSearchService.getMessagesSent(this.authorizationService.getUserId(), q)
                .stream()
                .map(this.messageSentResourceAssembler::toResource)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get received messages")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @GetMapping(value = "/received", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    List<MessageReceivedResource> getMessagesReceived(
            @ApiParam(value = "Search for a phrase")
            @RequestParam(value = "q", required = false) final String q
    ) {
        log.info("Called with q {}", q);

        return this.messageSearchService.getMessagesReceived(this.authorizationService.getUserId(), q)
                .stream()
                .map(this.messageReceivedResourceAssembler::toResource)
                .collect(Collectors.toList());
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

        if(!this.userSearchService.existsUserByUsername(username)) {
            final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();
            validationErrorDTO.addFieldError("to", this.bundle.getString("validation.noExistsUsername"));

            throw new FormBadRequestException(validationErrorDTO);
        }
    }
}
