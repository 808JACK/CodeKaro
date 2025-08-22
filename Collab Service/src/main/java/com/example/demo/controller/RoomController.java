/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestHeader
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.example.demo.controller;

import com.example.demo.dtos.CodeStateDTO;
import com.example.demo.dtos.CreateRoomRequest;
import com.example.demo.dtos.JoinRoomRequestDTO;
import com.example.demo.dtos.OTOperationDTO;
import com.example.demo.dtos.RoomCreatedResponse;
import com.example.demo.dtos.RoomDetailsResponse;
import com.example.demo.entities.OTOperation;
import com.example.demo.services.CompactionService;
import com.example.demo.services.OTServiceInterface;
import com.example.demo.services.RoomService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/rooms"})
public class RoomController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(RoomController.class);
    private final RoomService roomService;
    private final OTServiceInterface otService;
    private final CompactionService compactionService;

    public RoomController(RoomService roomService, OTServiceInterface otService, CompactionService compactionService) {
        this.roomService = roomService;
        this.otService = otService;
        this.compactionService = compactionService;
    }

    @PostMapping(value={"/create"})
    public ResponseEntity<RoomCreatedResponse> createRoom(@RequestBody CreateRoomRequest request, @RequestHeader(value="X-User-Id") Long userId) {
        try {
            log.info("Creating room for user {} with {} problems", (Object)userId, (Object)(request.getProblemIds() != null ? request.getProblemIds().size() : 0));
            RoomCreatedResponse response = this.roomService.createRoom(request, userId);
            return ResponseEntity.ok((RoomCreatedResponse) response);
        }
        catch (Exception e) {
            log.error("Error creating room: {}", (Object)e.getMessage(), (Object)e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value={"/join"})
    public ResponseEntity<RoomDetailsResponse> joinRoom(@RequestBody JoinRoomRequestDTO request, @RequestHeader(value="X-User-Id") Long userId) {
        try {
            String inviteCode = request.getInviteCode();
            log.info("User {} attempting to join room {}", (Object)userId, (Object)inviteCode);
            if (!this.roomService.isValidRoom(inviteCode)) {
                return ResponseEntity.notFound().build();
            }
            RoomDetailsResponse response = this.roomService.getRoomDetails(inviteCode, userId);
            log.info("User {} successfully joined room {}", (Object)userId, (Object)inviteCode);
            return ResponseEntity.ok((RoomDetailsResponse) response);
        }
        catch (Exception e) {
            log.error("Error joining room: {}", (Object)e.getMessage(), (Object)e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value={"/{inviteCode}/details"})
    public ResponseEntity<RoomDetailsResponse> getRoomDetails(@PathVariable String inviteCode, @RequestHeader(value="X-User-Id") Long userId) {
        try {
            if (!this.roomService.isValidRoom(inviteCode)) {
                return ResponseEntity.notFound().build();
            }
            RoomDetailsResponse response = this.roomService.getRoomDetails(inviteCode, userId);
            return ResponseEntity.ok((RoomDetailsResponse) response);
        }
        catch (Exception e) {
            log.error("Error getting room details: {}", (Object)e.getMessage(), (Object)e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value={"/{inviteCode}/validate"})
    public ResponseEntity<Boolean> validateRoom(@PathVariable String inviteCode) {
        try {
            boolean isValid = this.roomService.isValidRoom(inviteCode);
            return ResponseEntity.ok((Boolean) isValid);
        }
        catch (Exception e) {
            log.error("Error validating room: {}", (Object)e.getMessage(), (Object)e);
            return ResponseEntity.ok((Boolean) false);
        }
    }

    @GetMapping(value={"/{inviteCode}/operations"})
    public ResponseEntity<List<OTOperationDTO>> getRoomOperations(@PathVariable String inviteCode, @RequestParam(required=false) Integer fromVersion, @RequestHeader(value="X-User-Id") Long userId) {
        try {
            if (!this.roomService.isValidRoom(inviteCode)) {
                return ResponseEntity.notFound().build();
            }
            List<OTOperation> operations = fromVersion != null ? this.otService.getOperationsAfterVersion(inviteCode, fromVersion) : this.otService.getOperationsForRoom(inviteCode);
            List operationDTOs = operations.stream().map(op -> this.otService.convertToDTO((OTOperation)op, inviteCode)).collect(Collectors.toList());
            return ResponseEntity.ok(operationDTOs);
        }
        catch (Exception e) {
            log.error("Error getting operations for room {}: {}", new Object[]{inviteCode, e.getMessage(), e});
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value={"/{inviteCode}/operations/count"})
    public ResponseEntity<Long> getRoomOperationCount(@PathVariable String inviteCode, @RequestHeader(value="X-User-Id") Long userId) {
        try {
            if (!this.roomService.isValidRoom(inviteCode)) {
                return ResponseEntity.notFound().build();
            }
            long count = this.otService.getOperationCount(inviteCode);
            return ResponseEntity.ok((Long) count);
        }
        catch (Exception e) {
            log.error("Error getting operation count for room {}: {}", new Object[]{inviteCode, e.getMessage(), e});
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value={"/{inviteCode}/compact"})
    public ResponseEntity<String> manualCompaction(@PathVariable String inviteCode, @RequestHeader(value="X-User-Id") Long userId) {
        try {
            if (!this.roomService.isValidRoom(inviteCode)) {
                return ResponseEntity.notFound().build();
            }
            this.compactionService.compactRoomManually(inviteCode);
            return ResponseEntity.ok(((Object)("Compaction completed for room: " + inviteCode)).toString());
        }
        catch (Exception e) {
            log.error("Error during manual compaction for room {}: {}", new Object[]{inviteCode, e.getMessage(), e});
            return ResponseEntity.internalServerError().body(((Object)("Compaction failed: " + e.getMessage())).toString());
        }
    }

    @GetMapping(value={"/{inviteCode}/code/current"})
    public ResponseEntity<CodeStateDTO> getCurrentCode(@PathVariable String inviteCode, @RequestHeader(value="X-User-Id") Long userId) {
        try {
            if (!this.roomService.isValidRoom(inviteCode)) {
                return ResponseEntity.notFound().build();
            }
            String initialCode = "// Write your solution here\n";
            String currentCode = this.otService.reconstructCurrentCode(inviteCode, initialCode);
            CodeStateDTO codeState = CodeStateDTO.builder().roomCode(inviteCode).code(currentCode).language("java").version(Math.toIntExact(this.otService.getOperationCount(inviteCode))).timestamp(System.currentTimeMillis()).build();
            return ResponseEntity.ok((CodeStateDTO) codeState);
        }
        catch (Exception e) {
            log.error("Error getting current code for room {}: {}", new Object[]{inviteCode, e.getMessage(), e});
            return ResponseEntity.internalServerError().build();
        }
    }
}
