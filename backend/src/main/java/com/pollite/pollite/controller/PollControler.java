package com.pollite.pollite.controller;

import com.pollite.pollite.dto.PollDto;
import com.pollite.pollite.dto.PollInfoDto;
import com.pollite.pollite.dto.PollResults;
import com.pollite.pollite.exception.PollAnswerDoesNotExistException;
import com.pollite.pollite.exception.PollDoesNotExistException;
import com.pollite.pollite.exception.PollNotActiveException;
import com.pollite.pollite.exception.UserDoesNotExistException;
import com.pollite.pollite.exception.UserNotAuthorizedException;
import com.pollite.pollite.service.poll.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/poll")
@RequiredArgsConstructor
@CrossOrigin
public class PollControler {
    private final PollService pollService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createPoll(@Valid @RequestBody PollDto pollDTO, Principal principal)
            throws UserDoesNotExistException {
        pollService.addPoll(pollDTO, principal);
    }

    @GetMapping
    public ResponseEntity<Page<PollDto>> getPolls(Pageable pageable) {
        return ResponseEntity.ok(pollService.getPolls(pageable));
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<PollDto> getPoll(@PathVariable Long pollId) {
        return pollService.findPoll(pollId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<Page<PollInfoDto>> getUsersPollInfos(@PathVariable String username,
                                                               Pageable pageable,
                                                               Principal principal) {
        return ResponseEntity.ok(pollService.findUsersPollInfos(username, pageable, principal));
    }

    @DeleteMapping("/{pollId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deletePoll(@PathVariable Long pollId, Principal principal) throws UserNotAuthorizedException, UserDoesNotExistException, PollDoesNotExistException {
        pollService.deletePoll(pollId, principal);
    }

    @PutMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void editPoll(@RequestBody PollDto pollDto, Principal principal) throws UserDoesNotExistException, UserNotAuthorizedException {
        pollService.editPoll(pollDto, principal);
    }

    @GetMapping("/{pollId}/results")
    public ResponseEntity<PollResults> getPollResults(@PathVariable Long pollId) throws PollDoesNotExistException {
        var pollResults = pollService.getPollResults(pollId);
        return ResponseEntity.ok(pollResults);
    }

    @PostMapping("/{pollId}/vote/{pollAnswerId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<PollResults> vote(@PathVariable Long pollId, @PathVariable Long pollAnswerId) throws PollAnswerDoesNotExistException, PollNotActiveException, PollDoesNotExistException {
        return ResponseEntity.ok(pollService.vote(pollId, pollAnswerId));
    }
}
