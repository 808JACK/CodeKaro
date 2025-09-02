/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 */
package com.example.demo.services;

import com.example.demo.services.MinIOService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CodeExecutionService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(CodeExecutionService.class);
    private final MinIOService minIOService;
    private final CompetitiveProgrammingExecutionService competitiveProgrammingService;
    private final boolean dockerAvailable;
    private String reusableContainerId = null;
    private long lastContainerUse = 0L;
    private static final long CONTAINER_REUSE_TIMEOUT = 300000L;
    private static final String PYTHON_IMAGE = "python:3.11-alpine";

    public CodeExecutionService(MinIOService minIOService, CompetitiveProgrammingExecutionService competitiveProgrammingService) {
        this.minIOService = minIOService;
        this.competitiveProgrammingService = competitiveProgrammingService;
        this.dockerAvailable = this.checkDockerAvailability();
        if (this.dockerAvailable) {
            log.info("Docker CLI is available - real code execution enabled");
            this.ensurePythonImage();
            this.preCreateContainer();
        } else {
            log.warn("Docker CLI not available - using mock execution");
        }
    }

    private void preCreateContainer() {
        try {
            ProcessBuilder pb = new ProcessBuilder("docker", "run", "-d", "--memory=256m", "--cpus=1", PYTHON_IMAGE, "sleep", "3600");
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String containerId = reader.readLine();
            int exitCode = process.waitFor();
            if (exitCode == 0 && containerId != null) {
                this.reusableContainerId = containerId.trim();
                this.lastContainerUse = System.currentTimeMillis();
                log.info("Pre-created reusable container: {}", (Object)this.reusableContainerId);
            }
        }
        catch (Exception e) {
            log.warn("Failed to pre-create container: {}", (Object)e.getMessage());
        }
    }

    private boolean checkDockerAvailability() {
        try {
            ProcessBuilder pb = new ProcessBuilder("docker", "--version");
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        }
        catch (Exception e) {
            log.debug("Docker availability check failed: {}", (Object)e.getMessage());
            return false;
        }
    }

    private void ensurePythonImage() {
        try {
            ProcessBuilder pb = new ProcessBuilder("docker", "images", "-q", PYTHON_IMAGE);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String imageId = reader.readLine();
            if (imageId == null || imageId.trim().isEmpty()) {
                log.info("Python image not found, pulling {}...", (Object)PYTHON_IMAGE);
                this.pullPythonImage();
            } else {
                log.info("Python image {} is available", (Object)PYTHON_IMAGE);
            }
        }
        catch (Exception e) {
            log.warn("Failed to check Python image: {}", (Object)e.getMessage());
        }
    }

    private void pullPythonImage() {
        try {
            ProcessBuilder pb = new ProcessBuilder("docker", "pull", PYTHON_IMAGE);
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("Successfully pulled Python image");
            } else {
                log.warn("Failed to pull Python image, exit code: {}", (Object)exitCode);
            }
        }
        catch (Exception e) {
            log.error("Error pulling Python image: {}", (Object)e.getMessage());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ExecutionResult executeCode(String code, String language, String problemName, boolean isSubmit, long timeLimitMsPerTest, long memoryLimitMbPerTest) {
        List<TestCase> testCases;
        String containerId;
        Path tempDir;
        ExecutionResult result;
        block16: {
            block15: {
                result = new ExecutionResult();
                tempDir = null;
                containerId = null;
                log.info("Executing {} code for problem {}", (Object)language, (Object)problemName);
                List<MinIOService.TestCase> minioTestCases = this.minIOService.getTestCases(problemName, isSubmit);
                testCases = this.convertMinIOTestCases(minioTestCases);
                if (!testCases.isEmpty()) break block15;
                result.setOverallVerdict("No Test Cases");
                ExecutionResult executionResult = result;
                if (tempDir != null) {
                    this.deleteDirectory(tempDir);
                }
                return executionResult;
            }
            if (this.dockerAvailable) break block16;
            log.warn("Docker not available, using mock execution for problem {}", (Object)problemName);
            result.setOverallVerdict("AC");
            result.setError("Docker not available. Please install and start Docker Desktop to enable real code execution.");
            ArrayList<TestResult> mockResults = new ArrayList<TestResult>();
            for (int i = 0; i < testCases.size(); ++i) {
                mockResults.add(new TestResult(i + 1, "AC", 50L, "Mock output", ""));
            }
            result.setTestResults(mockResults);
            ExecutionResult i = result;
            if (tempDir != null) {
                this.deleteDirectory(tempDir);
            }
            return i;
        }
        try {
            boolean allAC;
            tempDir = Files.createTempDirectory("python-submission", new FileAttribute[0]);
            Path codeFile = tempDir.resolve("solution.py");
            Files.writeString(codeFile, (CharSequence)code, new OpenOption[0]);
            containerId = this.getOrCreateContainer();
            if (containerId == null) {
                throw new RuntimeException("Failed to get Docker container");
            }
            log.debug("Using container {} for problem {}", (Object)containerId, (Object)problemName);
            List<TestResult> testResults = this.executeTestCasesDirect(containerId, code, testCases, timeLimitMsPerTest, isSubmit, problemName);
            if (!isSubmit) {
                ArrayList<TestResult> visibleResults = new ArrayList<TestResult>();
                for (TestResult nunu : testResults) {
                    visibleResults.add(nunu);
                    if ("AC".equals(nunu.getVerdict())) continue;
                    log.debug("Stopping execution display on first failure for problem {}", (Object)problemName);
                    break;
                }
                testResults = visibleResults;
            }
            result.setOverallVerdict((allAC = testResults.stream().allMatch(tr -> "AC".equals(tr.getVerdict()))) ? "AC" : "Failed");
            result.setTestResults(testResults);
            log.info("Code execution completed for problem {}. Verdict: {}", (Object)problemName, (Object)result.getOverallVerdict());
            if (tempDir != null) {
                this.deleteDirectory(tempDir);
            }
        }
        catch (Exception e) {
            try {
                log.error("Error executing code for problem {}: {}", new Object[]{problemName, e.getMessage(), e});
                result.setOverallVerdict("CE");
                result.setError(e.getMessage());
                if (tempDir != null) {
                    this.deleteDirectory(tempDir);
                }
            }
            catch (Throwable throwable) {
                if (tempDir != null) {
                    this.deleteDirectory(tempDir);
                }
                throw throwable;
            }
        }
        return result;
    }

    private String getOrCreateContainer() {
        if (this.reusableContainerId != null) {
            long timeSinceLastUse = System.currentTimeMillis() - this.lastContainerUse;
            if (timeSinceLastUse < 300000L && this.isContainerRunning(this.reusableContainerId)) {
                this.lastContainerUse = System.currentTimeMillis();
                return this.reusableContainerId;
            }
            this.cleanupContainer(this.reusableContainerId);
            this.reusableContainerId = null;
        }
        this.preCreateContainer();
        return this.reusableContainerId;
    }

    private boolean isContainerRunning(String containerId) {
        try {
            ProcessBuilder pb = new ProcessBuilder("docker", "inspect", "-f", "{{.State.Running}}", containerId);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            return "true".equals(result);
        }
        catch (Exception e) {
            return false;
        }
    }

    private String preprocessInputForUser(String rawInput) {
        try {
            log.debug("Preprocessing input: '{}'", rawInput);
            
            // Handle competitive programming format conversion
            String processedInput = convertCompetitiveProgrammingInput(rawInput);
            
            return processedInput;
            
        } catch (Exception e) {
            log.warn("Error preprocessing input '{}': {}", rawInput, e.getMessage());
            return rawInput; // Always return original if any error occurs
        }
    }
    
    /**
     * Convert competitive programming input to proper format for code execution
     * Examples:
     * "2 7 11 15 9" -> "2 7 11 15\n9" (for two sum: array + target)
     * "1 2 3 4 5" -> "1 2 3 4 5" (for single array problems)
     */
    private String convertCompetitiveProgrammingInput(String input) {
        try {
            String[] parts = input.trim().split("\\s+");
            
            // For problems with multiple numbers, common patterns:
            if (parts.length >= 3) {
                // Check if this looks like a two-sum style problem (array + target)
                // Heuristic: if last number is much smaller than the sum of others, it's likely a target
                try {
                    int[] numbers = new int[parts.length];
                    for (int i = 0; i < parts.length; i++) {
                        numbers[i] = Integer.parseInt(parts[i]);
                    }
                    
                    // Simple heuristic: if we have 3+ numbers, treat last as target for two-sum style problems
                    if (numbers.length >= 3) {
                        // Array part (all but last)
                        StringBuilder arrayPart = new StringBuilder();
                        for (int i = 0; i < numbers.length - 1; i++) {
                            if (i > 0) arrayPart.append(" ");
                            arrayPart.append(numbers[i]);
                        }
                        // Target part (last number)
                        String targetPart = String.valueOf(numbers[numbers.length - 1]);
                        
                        return arrayPart.toString() + "\n" + targetPart;
                    }
                } catch (NumberFormatException e) {
                    // Not all numbers, return as-is
                    log.debug("Input contains non-numeric data, returning as-is");
                }
            }
            
            // For other cases, return as-is
            return input;
            
        } catch (Exception e) {
            log.debug("Error converting competitive programming input: {}", e.getMessage());
            return input;
        }
    }

    private String wrapUserCodeWithNullHandling(String userCode) {
        try {
            log.debug("Wrapping user code for competitive programming execution");
            
            StringBuilder wrappedCode = new StringBuilder();
            
            // Add competitive programming utilities
            wrappedCode.append("# Competitive Programming Utilities\n");
            wrappedCode.append("import sys\n");
            wrappedCode.append("import builtins\n");
            wrappedCode.append("_original_input = builtins.input\n\n");
            
            // Override input() for competitive programming
            wrappedCode.append("def input():\n");
            wrappedCode.append("    line = _original_input().strip()\n");
            wrappedCode.append("    return line\n\n");
            
            // Add helper functions for common competitive programming patterns
            wrappedCode.append("def read_ints():\n");
            wrappedCode.append("    \"\"\"Read space-separated integers from input\"\"\"\n");
            wrappedCode.append("    return list(map(int, input().split()))\n\n");
            
            wrappedCode.append("def read_int():\n");
            wrappedCode.append("    \"\"\"Read single integer from input\"\"\"\n");
            wrappedCode.append("    return int(input())\n\n");
            
            wrappedCode.append("def read_strings():\n");
            wrappedCode.append("    \"\"\"Read space-separated strings from input\"\"\"\n");
            wrappedCode.append("    return input().split()\n\n");
            
            // Replace builtins
            wrappedCode.append("builtins.input = input\n");
            wrappedCode.append("builtins.read_ints = read_ints\n");
            wrappedCode.append("builtins.read_int = read_int\n");
            wrappedCode.append("builtins.read_strings = read_strings\n\n");
            
            // Add user's code
            wrappedCode.append("# User's code starts here\n");
            wrappedCode.append(userCode);
            
            log.debug("Code wrapped successfully for competitive programming");
            return wrappedCode.toString();
            
        } catch (Exception e) {
            log.warn("Error wrapping user code: {}", e.getMessage());
            return userCode; // Return original code if wrapping fails
        }
    }

    private String detectFunctionName(String code) {
        String[] lines = code.split("\n");
        String lastFunctionName = null;
        for (String line : lines) {
            String funcName;
            String funcDef;
            int parenIndex;
            if (!(line = line.trim()).startsWith("def ") || !line.contains("(") || (parenIndex = (funcDef = line.substring(4)).indexOf("(")) <= 0 || (funcName = funcDef.substring(0, parenIndex).trim()).startsWith("_")) continue;
            lastFunctionName = funcName;
        }
        if (lastFunctionName != null) {
            log.debug("Detected function name: {}", lastFunctionName);
            return lastFunctionName;
        }
        log.warn("Could not detect function name, using default 'solution'");
        return "solution";
    }

    private String convertToPythonFormat(String output) {
        if (output == null) {
            return "None";
        }
        String converted = output.trim();
        if ("true".equals(converted)) {
            return "True";
        }
        if ("false".equals(converted)) {
            return "False";
        }
        if ("null".equals(converted)) {
            return "None";
        }
        return converted;
    }

    private String escapeForPythonString(String raw) {
        String s = raw.replace("\\", "\\\\");
        s = s.replace("\"", "\\\"");
        s = s.replace("\n", "\\n");
        return s;
    }

    private String createTestRunnerScript(String userCode, List<TestCase> testCases, String problemName) {
        StringBuilder script = new StringBuilder();
        script.append("# User's solution\n");
        script.append(userCode).append("\n\n");
        script.append("# Test runner\n");
        script.append("import json\n");
        script.append("import sys\n");
        script.append("import traceback\n\n");
        script.append("def run_tests():\n");
        script.append("    results = []\n");
        script.append("    \n");
        for (int i = 0; i < testCases.size(); ++i) {
            TestCase tc = testCases.get(i);
            String input = tc.getInput();
            String[] parts = input.split(", target = ");
            if (parts.length != 2) continue;
            String numsStr = parts[0].replace("nums = ", "");
            String targetStr = parts[1];
            script.append("    # Test case ").append(i + 1).append("\n");
            script.append("    try:\n");
            script.append("        nums = ").append(numsStr).append("\n");
            script.append("        target = ").append(targetStr).append("\n");
            script.append("        result = ").append(this.detectFunctionName(userCode)).append("(nums, target)\n");
            script.append("        expected = ").append(tc.getExpectedOutput()).append("\n");
            script.append("        verdict = 'AC' if result == expected else 'WA'\n");
            script.append("        results.append({\n");
            script.append("            'test': ").append(i + 1).append(",\n");
            script.append("            'verdict': verdict,\n");
            script.append("            'output': str(result),\n");
            script.append("            'expected': str(expected),\n");
            script.append("            'error': ''\n");
            script.append("        })\n");
            script.append("    except Exception as e:\n");
            script.append("        results.append({\n");
            script.append("            'test': ").append(i + 1).append(",\n");
            script.append("            'verdict': 'RE',\n");
            script.append("            'output': '',\n");
            script.append("            'expected': '").append(tc.getExpectedOutput()).append("',\n");
            script.append("            'error': str(e)\n");
            script.append("        })\n");
            script.append("    \n");
        }
        script.append("    return results\n\n");
        script.append("if __name__ == '__main__':\n");
        script.append("    try:\n");
        script.append("        test_results = run_tests()\n");
        script.append("        print(json.dumps(test_results))\n");
        script.append("    except Exception as e:\n");
        script.append("        print(json.dumps([{'test': 1, 'verdict': 'CE', 'output': '', 'expected': '', 'error': str(e)}]))\n");
        return script.toString();
    }

    private List<TestResult> executeTestCasesDirect(String containerId, String code, List<TestCase> testCases, long timeLimitMs, boolean isSubmit, String problemName) {
        // Use competitive programming execution for better handling of test cases
        log.info("Using competitive programming execution approach for problem: {}", problemName);
        return this.executeTestCasesCompetitiveProgramming(containerId, code, testCases, timeLimitMs, isSubmit, problemName);
    }
    
    /**
     * Execute test cases using competitive programming approach
     * Properly handles different problem types (TwoSum, LinkedList, etc.)
     */
    private List<TestResult> executeTestCasesCompetitiveProgramming(String containerId, String code, List<TestCase> testCases, long timeLimitMs, boolean isSubmit, String problemName) {
        List<TestResult> results = new ArrayList<TestResult>();
        
        try {
            long startTime = System.currentTimeMillis();
            
            // Convert test cases to competitive programming format
            List<CompetitiveProgrammingExecutionService.TestCase> cpTestCases = new ArrayList<>();
            for (TestCase tc : testCases) {
                cpTestCases.add(new CompetitiveProgrammingExecutionService.TestCase(tc.getInput(), tc.getExpectedOutput()));
            }
            
            // Generate the test script using competitive programming service
            String testScript = competitiveProgrammingService.generateTestScript(code, cpTestCases, problemName);
            
            log.info("Generated test script for problem: {}", problemName);
            log.debug("Test script preview: {}", testScript.length() > 500 ? testScript.substring(0, 500) + "..." : testScript);
            
            // Execute the test script in Docker
            ProcessBuilder pb = new ProcessBuilder("docker", "exec", containerId, "python3", "-c", testScript);
            Process process = pb.start();
            
            long totalTimeout = Math.min(timeLimitMs * (long)testCases.size(), 30000L); // Max 30 seconds
            boolean finished = process.waitFor(totalTimeout, TimeUnit.MILLISECONDS);
            long execTime = System.currentTimeMillis() - startTime;
            
            if (!finished) {
                process.destroyForcibly();
                for (int i = 1; i <= testCases.size(); i++) {
                    results.add(new TestResult(i, "TLE", execTime, "", "Time limit exceeded"));
                }
                return results;
            }
            
            String output = this.readStream(process.getInputStream());
            String error = this.readStream(process.getErrorStream());
            
            if (process.exitValue() != 0) {
                String errorType = this.determineErrorType(error);
                String cleanError = this.cleanErrorMessage(error);
                log.error("Docker execution failed for problem {}: exit code {}", problemName, process.exitValue());
                log.error("Error output: {}", error);
                log.error("Standard output: {}", output);
                
                for (int i = 1; i <= testCases.size(); i++) {
                    results.add(new TestResult(i, errorType, execTime, output, cleanError));
                }
                return results;
            }
            
            if (output.trim().isEmpty()) {
                for (int i = 1; i <= testCases.size(); i++) {
                    results.add(new TestResult(i, "RE", execTime, "", "No output produced. Check if your function returns a value."));
                }
                return results;
            }
            
            // Parse the JSON results
            results = this.parseTestResults(output.trim(), execTime);
            
        } catch (Exception e) {
            log.error("Error executing competitive programming test cases: {}", e.getMessage());
            results.add(new TestResult(1, "RE", 0L, "", e.getMessage()));
        }
        
        return results;
    }

    private List<TestResult> executeTestCasesStdinStdout(String containerId, String code, List<TestCase> testCases, long timeLimitMs, boolean isSubmit) {
        List<TestResult> results = new ArrayList<TestResult>();
        
        for (int i = 0; i < testCases.size(); i++) {
            TestCase tc = testCases.get(i);
            
            try {
                long startTime = System.currentTimeMillis();
                
                String rawInputData = tc.getInput();
                String inputData = this.preprocessInputForUser(rawInputData);
                log.info("Executing test case {} with raw input: '{}', processed input: '{}'", i + 1, rawInputData, inputData);
                log.info("Input data bytes: {}", java.util.Arrays.toString(inputData.getBytes()));
                
                // Inject null handling wrapper and execute user code
                String wrappedCode = this.wrapUserCodeWithNullHandling(code);
                ProcessBuilder pb = new ProcessBuilder("docker", "exec", "-i", containerId, "python3", "-c", wrappedCode);
                Process process = pb.start();
                
                // Provide input via stdin
                try (java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(process.getOutputStream())) {
                    writer.write(inputData + "\n");
                    writer.flush();
                }
                
                boolean finished = process.waitFor(timeLimitMs, TimeUnit.MILLISECONDS);
                long execTime = System.currentTimeMillis() - startTime;
                
                if (!finished) {
                    process.destroyForcibly();
                    results.add(new TestResult(i + 1, "TLE", execTime, "", "Time limit exceeded"));
                    continue;
                }
                
                String output = this.readStream(process.getInputStream()).trim();
                String error = this.readStream(process.getErrorStream()).trim();
                
                log.debug("Test case {} output: '{}', error: '{}'", i + 1, output, error);
                
                if (process.exitValue() != 0) {
                    String errorType = this.determineErrorType(error);
                    String cleanError = this.cleanErrorMessage(error);
                    results.add(new TestResult(i + 1, errorType, execTime, output, cleanError));
                    continue;
                }
                
                // Compare output with boolean normalization (True/true, False/false)
                String expectedOutput = tc.getExpectedOutput().trim();
                String normalizedExpected = this.normalizeOutputForCompare(expectedOutput);
                String normalizedActual = this.normalizeOutputForCompare(output);
                String verdict = normalizedActual.equals(normalizedExpected) ? "AC" : "WA";
                
                log.debug("Test case {} comparison: expected='{}', actual='{}', verdict='{}'", 
                         i + 1, expectedOutput, output, verdict);
                
                results.add(new TestResult(i + 1, verdict, execTime, output, ""));
                
            } catch (Exception e) {
                log.error("Error executing stdin/stdout test case {}: {}", i + 1, e.getMessage());
                results.add(new TestResult(i + 1, "RE", 0L, "", e.getMessage()));
            }
        }
        
        return results;
    }

    private String normalizeOutputForCompare(String value) {
        if (value == null) {
            return "";
        }
        String v = value.trim();
        // Strip common container chars and replace commas with spaces
        v = v.replace("[", " ").replace("]", " ").replace("(", " ").replace(")", " ");
        v = v.replace(",", " ");
        // Collapse all whitespace to single spaces
        v = v.replaceAll("\\s+", " ").trim();
        // Normalize booleans
        if (v.equalsIgnoreCase("true")) {
            return "true";
        }
        if (v.equalsIgnoreCase("false")) {
            return "false";
        }
        return v;
    }

    private List<TestResult> executeTestCasesWithParameters(String containerId, String code, List<TestCase> testCases, long timeLimitMs, boolean isSubmit) {
        List<TestResult> results = new ArrayList<TestResult>();
        try {
            long startTime = System.currentTimeMillis();
            StringBuilder script = new StringBuilder();
            script.append(code).append("\n\n");
            script.append("import json\n");
            // Helpers for output normalization and tree building
            script.append("def _to_str(x):\n");
            script.append("    try:\n");
            script.append("        if isinstance(x, (list, tuple)):\n");
            script.append("            try:\n");
            script.append("                return ' '.join(str(int(v)) for v in x)\n");
            script.append("            except Exception:\n");
            script.append("                return ' '.join(str(v) for v in x)\n");
            script.append("        if isinstance(x, bool):\n");
            script.append("            return 'true' if x else 'false'\n");
            script.append("        return str(x)\n");
            script.append("    except Exception:\n");
            script.append("        return str(x)\n");
            script.append("class TreeNode:\n");
            script.append("    def __init__(self, val=0, left=None, right=None):\n");
            script.append("        self.val = val\n");
            script.append("        self.left = left\n");
            script.append("        self.right = right\n");
            script.append("def _parse_token(t):\n");
            script.append("    if t is None: return None\n");
            script.append("    s = str(t).strip()\n");
            script.append("    if s in ('None','null','NULL','Null'): return None\n");
            script.append("    try:\n");
            script.append("        return int(s)\n");
            script.append("    except Exception:\n");
            script.append("        return s\n");
            script.append("def _build_tree(tokens):\n");
            script.append("    # tokens: list where None marks missing node, level-order\n");
            script.append("    if not tokens: return None\n");
            script.append("    tokens = list(tokens)\n");
            script.append("    root_val = _parse_token(tokens[0])\n");
            script.append("    if root_val is None: return None\n");
            script.append("    root = TreeNode(root_val)\n");
            script.append("    q = [root]\n");
            script.append("    idx = 1\n");
            script.append("    while q and idx < len(tokens):\n");
            script.append("        node = q.pop(0)\n");
            script.append("        if idx < len(tokens):\n");
            script.append("            lv = _parse_token(tokens[idx]); idx += 1\n");
            script.append("            if lv is not None:\n");
            script.append("                node.left = TreeNode(lv)\n");
            script.append("                q.append(node.left)\n");
            script.append("        if idx < len(tokens):\n");
            script.append("            rv = _parse_token(tokens[idx]); idx += 1\n");
            script.append("            if rv is not None:\n");
            script.append("                node.right = TreeNode(rv)\n");
            script.append("                q.append(node.right)\n");
            script.append("    return root\n");
            script.append("results = []\n");
            String functionName = this.detectFunctionName(code);
            // Build dynamic function resolver in Python
            script.append("__fn = None\n");
            script.append("__names = [\"").append(functionName).append("\", \"twoSum\", \"two_sum\", \"solution\", \"solve\", \"main\"]\n");
            script.append("for __name in __names:\n");
            script.append("    try:\n");
            script.append("        __c = globals().get(__name)\n");
            script.append("        if callable(__c):\n");
            script.append("            __fn = __c\n");
            script.append("            break\n");
            script.append("    except Exception:\n");
            script.append("        pass\n");
            script.append("if __fn is None:\n");
            script.append("    raise Exception('No callable function found (expected one of: ' + ', '.join(__names) + ')')\n");
            for (int i = 0; i < testCases.size(); ++i) {
                TestCase tc = testCases.get(i);
                String input = tc.getInput();
                script.append("try:\n");
                ParsedInput parsedInput = this.parseTestCaseInput(input);
                for (int j = 0; j < parsedInput.getVariableAssignments().size(); ++j) {
                    String varAssignment = parsedInput.getVariableAssignments().get(j);
                    String paramName = parsedInput.getParameterNames().get(j);
                    script.append("    ").append(varAssignment).append("\n");
                }
                if (parsedInput.getParameterNames().size() == 1 && "input_data".equals(parsedInput.getParameterNames().get(0))) {
                    script.append("    _parts = str(input_data).split()\n");
                    script.append("    _tokens = [None if p in ('None','null','NULL','Null') else int(p) if p.lstrip('+-').isdigit() else p for p in _parts]\n");
                    script.append("    _root = _build_tree(_tokens)\n");
                    script.append("    result = __fn(_root)\n");
                } else {
                    script.append("    result = __fn(");
                    script.append(String.join((CharSequence)", ", parsedInput.getParameterNames()));
                    script.append(")\n");
                }
                String expectedRaw = tc.getExpectedOutput() == null ? "" : tc.getExpectedOutput();
                String expectedEscaped = this.escapeForPythonString(expectedRaw.trim());
                script.append("    got = _to_str(result).strip()\n");
                script.append("    expected = \"").append(expectedEscaped).append("\"\n");
                script.append("    verdict = 'AC' if got == expected else 'WA'\n");
                script.append("    results.append({'test': ").append(i + 1).append(", 'verdict': verdict, 'output': got, 'expected': expected, 'error': ''})\n");
                script.append("except Exception as e:\n");
                script.append("    results.append({'test': ").append(i + 1).append(", 'verdict': 'RE', 'output': '', 'expected': '").append(tc.getExpectedOutput()).append("', 'error': str(e)})\n");
            }
            script.append("print(json.dumps(results))\n");
            ProcessBuilder pb = new ProcessBuilder("docker", "exec", containerId, "python3", "-c", script.toString());
            Process process = pb.start();
            long totalTimeout = Math.min(timeLimitMs * (long)testCases.size(), 10000L);
            boolean finished = process.waitFor(totalTimeout, TimeUnit.MILLISECONDS);
            long execTime = System.currentTimeMillis() - startTime;
            if (!finished) {
                process.destroyForcibly();
                for (int i = 1; i <= testCases.size(); ++i) {
                    results.add(new TestResult(i, "TLE", execTime, "", "Time limit exceeded"));
                }
                return results;
            }
            String output = this.readStream(process.getInputStream());
            String error = this.readStream(process.getErrorStream());
            if (process.exitValue() != 0) {
                String errorType = this.determineErrorType(error);
                String cleanError = this.cleanErrorMessage(error);
                log.debug("Execution failed with error type: {}, message: {}", (Object)errorType, (Object)cleanError);
                for (int i = 1; i <= testCases.size(); ++i) {
                    results.add(new TestResult(i, errorType, execTime, output, cleanError));
                }
                return results;
            }
            if (output.trim().isEmpty()) {
                for (int i = 1; i <= testCases.size(); ++i) {
                    results.add(new TestResult(i, "RE", execTime, "", "No output produced. Check if your function returns a value."));
                }
                return results;
            }
            results = this.parseTestResults(output.trim(), execTime);
        }
        catch (Exception e) {
            log.error("Error executing test cases: {}", (Object)e.getMessage());
            results.add(new TestResult(1, "RE", 0L, "", e.getMessage()));
        }
        return results;
    }

    private List<TestResult> executeAllTestCases(String containerId, int numTestCases, long timeLimitMs, boolean isSubmit) {
        List<TestResult> results;
        block9: {
            results = new ArrayList<TestResult>();
            try {
                long startTime = System.currentTimeMillis();
                ProcessBuilder pb = new ProcessBuilder("docker", "exec", containerId, "python3", "/app/test_runner.py");
                Process process = pb.start();
                long totalTimeout = timeLimitMs * (long)numTestCases + 5000L;
                boolean finished = process.waitFor(totalTimeout, TimeUnit.MILLISECONDS);
                long execTime = System.currentTimeMillis() - startTime;
                if (!finished) {
                    process.destroyForcibly();
                    for (int i = 1; i <= numTestCases; ++i) {
                        results.add(new TestResult(i, "TLE", execTime, "", "Time limit exceeded"));
                    }
                    return results;
                }
                String output = this.readStream(process.getInputStream());
                String error = this.readStream(process.getErrorStream());
                int exitCode = process.exitValue();
                if (exitCode != 0 || output.trim().isEmpty()) {
                    for (int i = 1; i <= numTestCases; ++i) {
                        results.add(new TestResult(i, "CE", execTime, output, error));
                    }
                    return results;
                }
                try {
                    String jsonOutput = output.trim();
                    if (jsonOutput.startsWith("[") && jsonOutput.endsWith("]")) {
                        results = this.parseTestResults(jsonOutput, execTime);
                        break block9;
                    }
                    results.add(new TestResult(1, "RE", execTime, output, "Invalid JSON output"));
                }
                catch (Exception e) {
                    log.error("Error parsing test results: {}", (Object)e.getMessage());
                    results.add(new TestResult(1, "RE", execTime, output, "JSON parsing error: " + e.getMessage()));
                }
            }
            catch (Exception e) {
                log.error("Error executing all test cases: {}", (Object)e.getMessage());
                results.add(new TestResult(1, "RE", 0L, "", e.getMessage()));
            }
        }
        return results;
    }

    private List<TestResult> parseTestResults(String jsonOutput, long totalTime) {
        ArrayList<TestResult> results = new ArrayList<TestResult>();
        try {
            String content = jsonOutput.substring(1, jsonOutput.length() - 1);
            String[] testObjects = content.split("\\},\\s*\\{");
            for (int i = 0; i < testObjects.length; ++i) {
                Object testObj = testObjects[i];
                if (i == 0 && !((String)testObj).startsWith("{")) {
                    testObj = "{" + (String)testObj;
                }
                if (i == testObjects.length - 1 && !((String)testObj).endsWith("}")) {
                    testObj = (String)testObj + "}";
                }
                if (i > 0 && i < testObjects.length - 1) {
                    testObj = "{" + (String)testObj + "}";
                }
                int testNum = this.extractIntValue((String)testObj, "test");
                String verdict = this.extractStringValue((String)testObj, "verdict");
                String output = this.extractStringValue((String)testObj, "output");
                String error = this.extractStringValue((String)testObj, "error");
                results.add(new TestResult(testNum, verdict, totalTime / (long)testObjects.length, output, error));
            }
        }
        catch (Exception e) {
            log.error("Error in simple JSON parsing: {}", (Object)e.getMessage());
            results.add(new TestResult(1, "RE", totalTime, jsonOutput, "Parsing error: " + e.getMessage()));
        }
        return results;
    }

    private int extractIntValue(String json, String key) {
        try {
            String pattern = "\"" + key + "\":\\s*(\\d+)";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(json);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        }
        catch (Exception e) {
            log.debug("Error extracting int value for key {}: {}", (Object)key, (Object)e.getMessage());
        }
        return 1;
    }

    private String extractStringValue(String json, String key) {
        try {
            String pattern = "\"" + key + "\":\\s*\"([^\"]*?)\"";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(json);
            if (m.find()) {
                return m.group(1);
            }
        }
        catch (Exception e) {
            log.debug("Error extracting string value for key {}: {}", (Object)key, (Object)e.getMessage());
        }
        return "";
    }

    private String determineErrorType(String error) {
        if (error == null || error.trim().isEmpty()) {
            return "RE";
        }
        String lowerError = error.toLowerCase();
        if (lowerError.contains("syntaxerror") || lowerError.contains("invalid syntax")) {
            return "CE";
        }
        if (lowerError.contains("indentationerror") || lowerError.contains("unexpected indent")) {
            return "CE";
        }
        if (lowerError.contains("nameerror")) {
            return "CE";
        }
        if (lowerError.contains("importerror") || lowerError.contains("modulenotfounderror")) {
            return "CE";
        }
        if (lowerError.contains("runtimeerror") || lowerError.contains("recursionerror") || lowerError.contains("indexerror") || lowerError.contains("keyerror") || lowerError.contains("typeerror") || lowerError.contains("valueerror") || lowerError.contains("attributeerror") || lowerError.contains("zerodivisionerror")) {
            return "RE";
        }
        if (lowerError.contains("memoryerror")) {
            return "MLE";
        }
        return "RE";
    }

    private String cleanErrorMessage(String error) {
        if (error == null || error.trim().isEmpty()) {
            return "Unknown error occurred";
        }
        String[] lines = error.split("\n");
        StringBuilder cleanError = new StringBuilder();
        boolean foundMainError = false;
        for (String line : lines) {
            if ((line = line.trim()).isEmpty() || line.startsWith("File \"<string>\"") || line.equals("Traceback (most recent call last):")) continue;
            line = line.replaceAll("File \"<string>\", line \\d+, in <module>", "");
            line = line.replaceAll("File \"<string>\", line \\d+", "");
            if ((line = line.trim()).isEmpty()) continue;
            if (foundMainError) {
                cleanError.append("\n");
            }
            cleanError.append(line);
            foundMainError = true;
        }
        Object result = cleanError.toString().trim();
        if (((String)result).isEmpty()) {
            for (String line : lines) {
                if (!line.contains("Error:") && !line.contains("Exception:")) continue;
                return line.trim();
            }
            return "Execution error occurred";
        }
        if (((String)result).length() > 200) {
            result = ((String)result).substring(0, 200) + "...";
        }
        return (String) result;
    }

    private String readStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));){
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString().trim();
    }

    private void cleanupContainer(String containerId) {
        try {
            ProcessBuilder stopPb = new ProcessBuilder("docker", "stop", containerId);
            stopPb.start().waitFor();
            ProcessBuilder rmPb = new ProcessBuilder("docker", "rm", containerId);
            rmPb.start().waitFor();
            log.debug("Container {} cleaned up", (Object)containerId);
        }
        catch (Exception e) {
            log.warn("Error cleaning up container {}: {}", (Object)containerId, (Object)e.getMessage());
        }
    }

    private List<TestCase> convertMinIOTestCases(List<MinIOService.TestCase> minioTestCases) {
        ArrayList<TestCase> testCases = new ArrayList<TestCase>();
        for (MinIOService.TestCase minioTestCase : minioTestCases) {
            testCases.add(new TestCase(minioTestCase.getInput().trim(), minioTestCase.getExpectedOutput().trim()));
        }
        log.info("Converted {} MinIO test cases to execution format", (Object)testCases.size());
        return testCases;
    }

    private void deleteDirectory(Path directory) {
        try {
            Files.walk(directory, new FileVisitOption[0]).sorted((a, b) -> b.compareTo((Path)a)).forEach(path -> {
                try {
                    Files.delete(path);
                }
                catch (Exception e) {
                    log.warn("Failed to delete {}: {}", path, (Object)e.getMessage());
                }
            });
        }
        catch (Exception e) {
            log.warn("Error deleting directory {}: {}", (Object)directory, (Object)e.getMessage());
        }
    }

    private ParsedInput parseTestCaseInput(String input) {
        ParsedInput parsed = new ParsedInput();
        try {
            // Handle common two-line format: first line = array numbers, second line = target
            String[] lines = input.split("\\r?\\n");
            String firstNonEmpty = null;
            String secondNonEmpty = null;
            for (String ln : lines) {
                String t = ln.trim();
                if (t.isEmpty()) continue;
                if (firstNonEmpty == null) {
                    firstNonEmpty = t;
                } else {
                    secondNonEmpty = t;
                    break;
                }
            }
            if (firstNonEmpty != null && (secondNonEmpty != null || input.contains("\n"))) {
                // Build Python list from first line numbers
                String[] nums = firstNonEmpty.split("\\s+");
                StringBuilder numsList = new StringBuilder();
                numsList.append("[");
                boolean first = true;
                for (String token : nums) {
                    String tk = token.trim();
                    if (tk.isEmpty()) continue;
                    if (!first) numsList.append(", ");
                    numsList.append(tk);
                    first = false;
                }
                numsList.append("]");
                parsed.addParameter("nums", "nums = " + numsList.toString());
                if (secondNonEmpty != null) {
                    parsed.addParameter("target", "target = " + secondNonEmpty.trim());
                }
                return parsed;
            }

            String[] assignments;
            for (String assignment : assignments = input.split(",\\s*(?=\\w+\\s*=)")) {
                String[] parts;
                if (!(assignment = assignment.trim()).contains(" = ") || (parts = assignment.split("\\s*=\\s*", 2)).length != 2) continue;
                String varName = parts[0].trim();
                String varValue = parts[1].trim();
                parsed.addParameter(varName, varName + " = " + varValue);
            }
            if (parsed.getParameterNames().isEmpty()) {
                log.warn("Could not parse input format: {}", (Object)input);
                parsed.addParameter("input_data", "input_data = " + input);
            }
        }
        catch (Exception e) {
            log.error("Error parsing test case input: {}", (Object)e.getMessage());
            parsed.addParameter("input_data", "input_data = " + input);
        }
        return parsed;
    }

    public static class ExecutionResult {
        private String overallVerdict;
        private List<TestResult> testResults;
        private String error;

        public String getOverallVerdict() {
            return this.overallVerdict;
        }

        public void setOverallVerdict(String overallVerdict) {
            this.overallVerdict = overallVerdict;
        }

        public List<TestResult> getTestResults() {
            return this.testResults;
        }

        public void setTestResults(List<TestResult> testResults) {
            this.testResults = testResults;
        }

        public String getError() {
            return this.error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    public static class TestResult {
        private int testNumber;
        private String verdict;
        private long timeMs;
        private String output;
        private String error;

        public TestResult(int testNumber, String verdict, long timeMs, String output, String error) {
            this.testNumber = testNumber;
            this.verdict = verdict;
            this.timeMs = timeMs;
            this.output = output;
            this.error = error;
        }

        public int getTestNumber() {
            return this.testNumber;
        }

        public String getVerdict() {
            return this.verdict;
        }

        public long getTimeMs() {
            return this.timeMs;
        }

        public String getOutput() {
            return this.output;
        }

        public String getError() {
            return this.error;
        }
    }

    public static class TestCase {
        private String input;
        private String expectedOutput;

        public TestCase(String input, String expectedOutput) {
            this.input = input;
            this.expectedOutput = expectedOutput;
        }

        public String getInput() {
            return this.input;
        }

        public String getExpectedOutput() {
            return this.expectedOutput;
        }
    }

    private static class ParsedInput {
        private final List<String> parameterNames = new ArrayList<String>();
        private final List<String> variableAssignments = new ArrayList<String>();

        private ParsedInput() {
        }

        public void addParameter(String name, String assignment) {
            this.parameterNames.add(name);
            this.variableAssignments.add(assignment);
        }

        public List<String> getParameterNames() {
            return this.parameterNames;
        }

        public List<String> getVariableAssignments() {
            return this.variableAssignments;
        }
    }
}
