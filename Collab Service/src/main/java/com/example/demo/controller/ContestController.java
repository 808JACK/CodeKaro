/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestHeader
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.example.demo.controller;

import com.example.demo.dtos.CodeExecutionRequest;
import com.example.demo.dtos.CodeExecutionResponse;
import com.example.demo.dtos.ContestDetailsResponse;
import com.example.demo.dtos.CreateContestRequest;
import com.example.demo.dtos.RoomCreatedResponse;
import com.example.demo.services.CodeExecutionService;
import com.example.demo.services.ContestService;
import java.util.ArrayList;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/contests"})
public class ContestController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ContestController.class);
    private final ContestService contestService;
    @Autowired(required=false)
    private CodeExecutionService codeExecutionService;

    public ContestController(ContestService contestService) {
        this.contestService = contestService;
    }

    @PostMapping(value={"/create"})
    public ResponseEntity<RoomCreatedResponse> createContest(@RequestBody CreateContestRequest request, @RequestHeader(value="X-User-Id") Long userId) {
        try {
            log.info("Creating contest for user {} with {} problems", (Object)userId, (Object)(request.getProblemIds() != null ? request.getProblemIds().size() : 0));
            RoomCreatedResponse response = this.contestService.createContest(request, userId);
            return ResponseEntity.ok((RoomCreatedResponse) response);
        }
        catch (Exception e) {
            log.error("Error creating contest: {}", (Object)e.getMessage(), (Object)e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value={"/{contestCode}/join"})
    public ResponseEntity<ContestDetailsResponse> joinContest(@PathVariable String contestCode, @RequestHeader(value="X-User-Id") Long userId) {
        try {
            log.info("User {} attempting to join contest {}", (Object)userId, (Object)contestCode);
            if (!this.contestService.isValidContest(contestCode)) {
                return ResponseEntity.notFound().build();
            }
            ContestDetailsResponse response = this.contestService.joinContest(contestCode, userId);
            log.info("User {} successfully joined contest {}", (Object)userId, (Object)contestCode);
            return ResponseEntity.ok((ContestDetailsResponse) response);
        }
        catch (Exception e) {
            log.error("Error joining contest: {}", (Object)e.getMessage(), (Object)e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value={"/{contestCode}/details"})
    public ResponseEntity<ContestDetailsResponse> getContestDetails(@PathVariable String contestCode, @RequestHeader(value="X-User-Id") Long userId) {
        try {
            if (!this.contestService.isValidContest(contestCode)) {
                return ResponseEntity.notFound().build();
            }
            ContestDetailsResponse response = this.contestService.getContestDetails(contestCode, userId);
            return ResponseEntity.ok((ContestDetailsResponse) response);
        }
        catch (Exception e) {
            log.error("Error getting contest details: {}", (Object)e.getMessage(), (Object)e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value={"/{contestCode}/validate"})
    public ResponseEntity<Boolean> validateContest(@PathVariable String contestCode) {
        try {
            boolean isValid = this.contestService.isValidContest(contestCode);
            return ResponseEntity.ok((Boolean) isValid);
        }
        catch (Exception e) {
            log.error("Error validating contest: {}", (Object)e.getMessage(), (Object)e);
            return ResponseEntity.ok((Boolean) false);
        }
    }

    @PostMapping(value={"/code/run"})
    public ResponseEntity<CodeExecutionResponse> runCode(@RequestBody CodeExecutionRequest request) {
        try {
            log.info("Running code for problem: {}", (Object)request.getProblemName());
            if (this.codeExecutionService == null) {
                log.warn("CodeExecutionService not available - Docker not configured");
                CodeExecutionResponse response = new CodeExecutionResponse();
                response.setOverallVerdict("CE");
                response.setTestResults(new ArrayList<CodeExecutionResponse.TestResult>());
                response.setError("Code execution service not available. Please ensure Docker is running and restart the service.");
                return ResponseEntity.ok((CodeExecutionResponse) response);
            }
            CodeExecutionService.ExecutionResult result = this.codeExecutionService.executeCode(request.getCode(), request.getLanguage(), request.getProblemName(), false, request.getTimeLimitMs(), request.getMemoryLimitMb());
            CodeExecutionResponse response = new CodeExecutionResponse();
            response.setOverallVerdict(result.getOverallVerdict());
            if (result.getTestResults() != null) {
                response.setTestResults(result.getTestResults().stream().map(tr -> {
                    CodeExecutionResponse.TestResult testResult = new CodeExecutionResponse.TestResult();
                    testResult.setTestNumber(tr.getTestNumber());
                    testResult.setVerdict(tr.getVerdict());
                    testResult.setTimeMs(tr.getTimeMs());
                    testResult.setOutput(tr.getOutput());
                    testResult.setError(tr.getError());
                    return testResult;
                }).toList());
            } else {
                response.setTestResults(new ArrayList<CodeExecutionResponse.TestResult>());
            }
            response.setError(result.getError());
            return ResponseEntity.ok((CodeExecutionResponse) response);
        }
        catch (Exception e) {
            log.error("Error running code: {}", (Object)e.getMessage(), (Object)e);
            CodeExecutionResponse errorResponse = new CodeExecutionResponse();
            errorResponse.setOverallVerdict("CE");
            errorResponse.setTestResults(new ArrayList<CodeExecutionResponse.TestResult>());
            errorResponse.setError(e.getMessage());
            return ResponseEntity.ok((CodeExecutionResponse) errorResponse);
        }
    }

    @PostMapping(value={"/code/submit"})
    public ResponseEntity<CodeExecutionResponse> submitCode(@RequestBody CodeExecutionRequest request) {
        try {
            log.info("Submitting code for problem: {}", (Object)request.getProblemName());
            if (this.codeExecutionService == null) {
                log.warn("CodeExecutionService not available - Docker not configured");
                CodeExecutionResponse response = new CodeExecutionResponse();
                response.setOverallVerdict("CE");
                response.setTestResults(new ArrayList<CodeExecutionResponse.TestResult>());
                response.setError("Code execution service not available. Please ensure Docker is running and restart the service.");
                return ResponseEntity.ok((CodeExecutionResponse) response);
            }
            CodeExecutionService.ExecutionResult result = this.codeExecutionService.executeCode(request.getCode(), request.getLanguage(), request.getProblemName(), true, request.getTimeLimitMs(), request.getMemoryLimitMb());
            CodeExecutionResponse response = new CodeExecutionResponse();
            response.setOverallVerdict(result.getOverallVerdict());
            if (result.getTestResults() != null) {
                response.setTestResults(result.getTestResults().stream().map(tr -> {
                    CodeExecutionResponse.TestResult testResult = new CodeExecutionResponse.TestResult();
                    testResult.setTestNumber(tr.getTestNumber());
                    testResult.setVerdict(tr.getVerdict());
                    testResult.setTimeMs(tr.getTimeMs());
                    testResult.setOutput(tr.getOutput());
                    testResult.setError(tr.getError());
                    return testResult;
                }).toList());
            } else {
                response.setTestResults(new ArrayList<CodeExecutionResponse.TestResult>());
            }
            response.setError(result.getError());
            return ResponseEntity.ok((CodeExecutionResponse) response);
        }
        catch (Exception e) {
            log.error("Error submitting code: {}", (Object)e.getMessage(), (Object)e);
            CodeExecutionResponse errorResponse = new CodeExecutionResponse();
            errorResponse.setOverallVerdict("CE");
            errorResponse.setTestResults(new ArrayList<CodeExecutionResponse.TestResult>());
            errorResponse.setError(e.getMessage());
            return ResponseEntity.ok((CodeExecutionResponse) errorResponse);
        }
    }
}
