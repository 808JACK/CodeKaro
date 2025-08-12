/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.minio.GetObjectArgs
 *  io.minio.GetObjectArgs$Builder
 *  io.minio.GetObjectResponse
 *  io.minio.ListObjectsArgs
 *  io.minio.ListObjectsArgs$Builder
 *  io.minio.MinioClient
 *  io.minio.Result
 *  io.minio.messages.Item
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.stereotype.Service
 */
package com.example.demo.services;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MinIOService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(MinIOService.class);
    private final MinioClient minioClient;
    @Value(value="${minio.bucket.testcases:testcases}")
    private String testCasesBucket;

    public MinIOService(@Value(value="${minio.url:http://localhost:9000}") String minioUrl, @Value(value="${minio.access-key:minioadmin}") String accessKey, @Value(value="${minio.secret-key:minioadmin}") String secretKey) {
        this.minioClient = MinioClient.builder().endpoint(minioUrl).credentials(accessKey, secretKey).build();
    }

    public List<String> getTestCaseInputs(String problemName) {
        ArrayList<String> inputs = new ArrayList<String>();
        try {
            String prefix = problemName + "/input/";
            Iterable<Result<Item>> results = this.minioClient.listObjects((ListObjectsArgs)((ListObjectsArgs.Builder)ListObjectsArgs.builder().bucket(this.testCasesBucket)).prefix(prefix).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                String objectName = item.objectName();
                if (!objectName.endsWith(".txt")) continue;
                String content = this.getFileContent(objectName);
                inputs.add(content);
                log.debug("Loaded input file: {}", objectName);
            }
            log.info("Loaded {} input files for problem {}", (Object)inputs.size(), (Object)problemName);
        }
        catch (Exception e) {
            log.error("Error loading test case inputs for problem {}: {}", new Object[]{problemName, e.getMessage(), e});
        }
        return inputs;
    }

    public List<String> getTestCaseOutputs(String problemName) {
        ArrayList<String> outputs = new ArrayList<String>();
        try {
            String prefix = problemName + "/output/";
            Iterable<Result<Item>> results = this.minioClient.listObjects((ListObjectsArgs)((ListObjectsArgs.Builder)ListObjectsArgs.builder().bucket(this.testCasesBucket)).prefix(prefix).build());
            for (Result<Item> result : results) {
                Item item = result.get();
                String objectName = item.objectName();
                if (!objectName.endsWith(".txt")) continue;
                String content = this.getFileContent(objectName);
                outputs.add(content);
                log.debug("Loaded output file: {}", (Object)objectName);
            }
            log.info("Loaded {} output files for problem {}", (Object)outputs.size(), (Object)problemName);
        }
        catch (Exception e) {
            log.error("Error loading test case outputs for problem {}: {}", new Object[]{problemName, e.getMessage(), e});
        }
        return outputs;
    }

    public List<TestCase> getTestCases(String problemName, boolean includeHidden) {
        List<TestCase> testCases = new ArrayList<>();
        try {
            List<String> inputs = this.getTestCaseInputs(problemName);
            List<String> outputs = this.getTestCaseOutputs(problemName);
            if (!inputs.isEmpty() && !outputs.isEmpty()) {
                int testCount = Math.min(inputs.size(), outputs.size());
                for (int i = 0; i < testCount; ++i) {
                    testCases.add(new TestCase(inputs.get(i), outputs.get(i), i < 2));
                }
            } else {
                testCases = this.parseCombinetTestCaseFormat(problemName, includeHidden);
            }
            if (!includeHidden) {
                testCases = testCases.stream().filter(TestCase::isVisible).toList();
            }
            log.info("Loaded {} test cases for problem {} (includeHidden: {})", new Object[]{testCases.size(), problemName, includeHidden});
        }
        catch (Exception e) {
            log.error("Error loading test cases for problem {}: {}", new Object[]{problemName, e.getMessage(), e});
        }
        return testCases;
    }

    private List<TestCase> parseCombinetTestCaseFormat(String problemName, boolean includeHidden) {
        List<TestCase> testCases;
        block6: {
            testCases = new ArrayList<TestCase>();
            try {
                String[] possiblePaths;
                for (String path : possiblePaths = new String[]{problemName + ".txt", problemName + "/testcases.txt", problemName + "/test.txt", problemName}) {
                    try {
                        String content = this.getFileContent(path).trim();
                        if (content.isEmpty()) continue;
                        log.info("Found test case file at: {}", (Object)path);
                        testCases = this.parseCombinedContent(content);
                        if (testCases.isEmpty()) continue;
                        break;
                    }
                    catch (Exception e) {
                        log.debug("File not found at path: {} - {}", (Object)path, (Object)e.getMessage());
                    }
                }
                if (!testCases.isEmpty()) break block6;
                Iterable<Result<Item>> results = this.minioClient.listObjects((ListObjectsArgs)((ListObjectsArgs.Builder)ListObjectsArgs.builder().bucket(this.testCasesBucket)).build());
                for (Result<Item> result : results) {
                    Item item = result.get();
                    String objectName = item.objectName();
                    if (!objectName.contains(problemName) || !objectName.endsWith(".txt")) continue;
                    log.info("Found potential test case file: {}", (Object)objectName);
                    String content = this.getFileContent(objectName).trim();
                    testCases = this.parseCombinedContent(content);
                    if (testCases.isEmpty()) continue;
                    break;
                }
            }
            catch (Exception e) {
                log.error("Error parsing combined test case format for problem {}: {}", new Object[]{problemName, e.getMessage(), e});
            }
        }
        return testCases;
    }

    private List<TestCase> parseCombinedContent(String content) {
        List<TestCase> testCases = new ArrayList<TestCase>();
        try {
            log.info("Parsing content: {}", (Object)content);
            
            // Try stdin/stdout format first
            testCases = this.parseStdinStdoutFormat(content);
            if (!testCases.isEmpty()) {
                return testCases;
            }
            
            // Fallback to old parameter format
            Pattern pattern = Pattern.compile("([^\u2192]+?)\\s*\u2192\\s*output:\\s*([^\u2192]+?)(?=\\w+\\s*=|$)");
            Matcher matcher = pattern.matcher(content);
            int testNumber = 0;
            while (matcher.find()) {
                String parameters = matcher.group(1).trim();
                String expectedOutput = matcher.group(2).trim();
                testCases.add(new TestCase(parameters, expectedOutput, testNumber < 2));
                log.debug("Parsed test case {}: input='{}', expected='{}'", new Object[]{++testNumber, parameters, expectedOutput});
            }
            log.info("Successfully parsed {} test cases from arrow format", (Object)testCases.size());
            if (testCases.isEmpty()) {
                log.warn("No test cases found with arrow pattern, trying fallback formats...");
                testCases = this.parseFallbackFormats(content);
            }
        }
        catch (Exception e) {
            log.error("Error parsing combined content: {}", (Object)e.getMessage(), (Object)e);
        }
        return testCases;
    }

    private List<TestCase> parseStdinStdoutFormat(String content) {
        List<TestCase> testCases = new ArrayList<TestCase>();
        try {
            log.info("Attempting to parse stdin/stdout format");
            
            // Try new clean format first (# Test Case N ... EXPECTED: ...)
            testCases = this.parseCleanBlockFormat(content);
            if (!testCases.isEmpty()) {
                return testCases;
            }
            
            // Try compact format (# Test Case 1...EXPECTED:...# Test Case 2...)
            testCases = this.parseCompactBlockFormat(content);
            if (!testCases.isEmpty()) {
                return testCases;
            }
            
            // Try simple line format (numbers EXPECTED: output)
            testCases = this.parseSimpleLineFormat(content);
            if (!testCases.isEmpty()) {
                return testCases;
            }
            
            // Fallback to arrow format (→ output:)
            String[] testBlocks = content.split("→\\s*output:");
            
            if (testBlocks.length < 2) {
                log.debug("No stdin/stdout format detected");
                return testCases;
            }
            
            for (int i = 0; i < testBlocks.length - 1; i++) {
                String inputSection = testBlocks[i].trim();
                String outputSection = testBlocks[i + 1].trim();
                
                // Extract input (everything in current section, excluding previous output)
                String[] inputLines = inputSection.split("\n");
                StringBuilder inputBuilder = new StringBuilder();
                
                // Skip lines that look like previous output, take actual input lines
                boolean foundInput = false;
                for (String line : inputLines) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        // Skip lines that are clearly output from previous test case
                        if (!foundInput && (line.matches("\\d+\\s+\\d+.*") || line.matches("\\[.*\\]") || line.matches("\\d+"))) {
                            foundInput = true;
                        }
                        if (foundInput) {
                            if (inputBuilder.length() > 0) inputBuilder.append("\n");
                            inputBuilder.append(line);
                        }
                    }
                }
                
                // If no clear input found, take last few lines as input
                if (!foundInput && inputLines.length > 0) {
                    // Take last 1-3 lines as input (common for most problems)
                    int startIdx = Math.max(0, inputLines.length - 3);
                    for (int j = startIdx; j < inputLines.length; j++) {
                        String line = inputLines[j].trim();
                        if (!line.isEmpty()) {
                            if (inputBuilder.length() > 0) inputBuilder.append("\n");
                            inputBuilder.append(line);
                        }
                    }
                }
                
                // Extract expected output (first non-empty line after →)
                String[] outputLines = outputSection.split("\n");
                String expectedOutput = "";
                for (String line : outputLines) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        expectedOutput = line;
                        break;
                    }
                }
                
                if (!inputBuilder.toString().trim().isEmpty() && !expectedOutput.isEmpty()) {
                    testCases.add(new TestCase(inputBuilder.toString().trim(), expectedOutput, i < 2));
                    log.debug("Parsed arrow format test case {}: input='{}', expected='{}'", 
                             i + 1, inputBuilder.toString().trim(), expectedOutput);
                }
            }
            
            log.info("Successfully parsed {} test cases from stdin/stdout format", (Object)testCases.size());
        } catch (Exception e) {
            log.error("Error parsing stdin/stdout format: {}", (Object)e.getMessage(), (Object)e);
        }
        return testCases;
    }

    private List<TestCase> parseCleanBlockFormat(String content) {
        List<TestCase> testCases = new ArrayList<TestCase>();
        try {
            log.info("Attempting to parse clean block format");
            log.debug("Content to parse: '{}'", content);
            
            // Handle case where line breaks might be missing - add line breaks before key patterns
            String normalizedContent = content.replaceAll("#\\s*Test\\s*Case", "\n# Test Case")
                                            .replaceAll("EXPECTED:", "\nEXPECTED:")
                                            .replaceAll("(\\d+)EXPECTED:", "$1\nEXPECTED:");  // Separate numbers from EXPECTED
            
            log.debug("Normalized content: '{}'", normalizedContent);
            
            // Split into test case blocks
            String[] testCaseBlocks = normalizedContent.split("(?=# Test Case)");
            
            for (String block : testCaseBlocks) {
                block = block.trim();
                if (block.isEmpty() || !block.startsWith("# Test Case")) continue;
                
                log.debug("Processing block: '{}'", block);
                
                // Extract test case number and content
                String[] lines = block.split("\n");
                StringBuilder inputBuilder = new StringBuilder();
                String expectedOutput = "";
                boolean foundExpected = false;
                
                for (String line : lines) {
                    line = line.trim();
                    if (line.isEmpty()) continue;
                    
                    if (line.startsWith("# Test Case")) {
                        // Skip test case header
                        continue;
                    } else if (line.startsWith("EXPECTED:")) {
                        // Extract expected output
                        expectedOutput = line.substring("EXPECTED:".length()).trim();
                        foundExpected = true;
                        break;
                    } else {
                        // This is input data - need to separate numbers properly
                        // For format like "4 5 1 95" we need to split into "4 5 1 9" and "5"
                        String[] numbers = line.split("\\s+");
                        if (numbers.length > 1) {
                            // For linked list problems: all but last number on first line, last number on second line
                            StringBuilder firstLine = new StringBuilder();
                            for (int i = 0; i < numbers.length - 1; i++) {
                                if (firstLine.length() > 0) firstLine.append(" ");
                                firstLine.append(numbers[i]);
                            }
                            String secondLine = numbers[numbers.length - 1];
                            
                            if (inputBuilder.length() > 0) inputBuilder.append("\n");
                            inputBuilder.append(firstLine.toString()).append("\n").append(secondLine);
                        } else {
                            // Single number or non-numeric data
                            if (inputBuilder.length() > 0) inputBuilder.append("\n");
                            inputBuilder.append(line);
                        }
                    }
                }
                
                // If EXPECTED: is on same line as input, split it
                if (!foundExpected && inputBuilder.toString().contains("EXPECTED:")) {
                    String fullInput = inputBuilder.toString();
                    int expectedIndex = fullInput.indexOf("EXPECTED:");
                    String actualInput = fullInput.substring(0, expectedIndex).trim();
                    expectedOutput = fullInput.substring(expectedIndex + "EXPECTED:".length()).trim();
                    inputBuilder = new StringBuilder(actualInput);
                    foundExpected = true;
                }
                
                if (!inputBuilder.toString().trim().isEmpty() && !expectedOutput.isEmpty()) {
                    testCases.add(new TestCase(inputBuilder.toString().trim(), expectedOutput, testCases.size() < 2));
                    log.debug("Parsed clean format test case {}: input='{}', expected='{}'", 
                             testCases.size(), inputBuilder.toString().trim(), expectedOutput);
                }
            }
            
            log.info("Successfully parsed {} test cases from clean block format", (Object)testCases.size());
        } catch (Exception e) {
            log.error("Error parsing clean block format: {}", (Object)e.getMessage(), (Object)e);
        }
        return testCases;
    }

    private List<TestCase> parseCompactBlockFormat(String content) {
        List<TestCase> testCases = new ArrayList<TestCase>();
        try {
            log.info("Attempting to parse compact block format");
            
            // Pattern to match: # Test Case N<numbers>EXPECTED: <output>
            Pattern pattern = Pattern.compile("#\\s*Test\\s*Case\\s*\\d+([^#]*?)EXPECTED:\\s*([^#]+?)(?=#\\s*Test\\s*Case|$)");
            Matcher matcher = pattern.matcher(content);
            
            int testNumber = 0;
            while (matcher.find()) {
                String inputSection = matcher.group(1).trim();
                String expectedOutput = matcher.group(2).trim();
                
                log.debug("Raw input section: '{}'", inputSection);
                
                // Extract all numbers from the input section
                Pattern numberPattern = Pattern.compile("\\d+");
                Matcher numberMatcher = numberPattern.matcher(inputSection);
                List<String> numbers = new ArrayList<String>();
                
                while (numberMatcher.find()) {
                    numbers.add(numberMatcher.group());
                }
                
                log.debug("Extracted numbers: {}", numbers);
                
                // For linked list problems: all but last number as array, last number as target
                if (numbers.size() >= 2) {
                    StringBuilder firstLine = new StringBuilder();
                    for (int i = 0; i < numbers.size() - 1; i++) {
                        if (firstLine.length() > 0) firstLine.append(" ");
                        firstLine.append(numbers.get(i));
                    }
                    String secondLine = numbers.get(numbers.size() - 1);
                    String inputStr = firstLine.toString() + "\n" + secondLine;
                    
                    testCases.add(new TestCase(inputStr, expectedOutput, testNumber < 2));
                    log.debug("Parsed compact format test case {}: input='{}', expected='{}'", 
                             testNumber + 1, inputStr, expectedOutput);
                    testNumber++;
                } else if (numbers.size() == 1) {
                    // Single number input
                    testCases.add(new TestCase(numbers.get(0), expectedOutput, testNumber < 2));
                    log.debug("Parsed compact format test case {}: input='{}', expected='{}'", 
                             testNumber + 1, numbers.get(0), expectedOutput);
                    testNumber++;
                }
            }
            
            log.info("Successfully parsed {} test cases from compact block format", (Object)testCases.size());
        } catch (Exception e) {
            log.error("Error parsing compact block format: {}", (Object)e.getMessage(), (Object)e);
        }
        return testCases;
    }

    private List<TestCase> parseSimpleLineFormat(String content) {
        List<TestCase> testCases = new ArrayList<TestCase>();
        try {
            log.info("Attempting to parse simple line format");
            log.debug("Raw content: '{}'", content);
            
            // Try to parse INPUT/OUTPUT format first
            if (content.contains("INPUT:") && content.contains("OUTPUT:")) {
                testCases = parseInputOutputFormat(content);
                if (!testCases.isEmpty()) {
                    log.info("Successfully parsed {} test cases from INPUT/OUTPUT format", (Object)testCases.size());
                    return testCases;
                }
            }
            
            // Universal format: test cases separated by blank lines
            // Each test case: multiple input lines + last line is expected output
            // Handle different line endings and multiple blank lines
            String[] testCaseBlocks = content.split("\\r?\\n\\s*\\r?\\n");
            
            for (String block : testCaseBlocks) {
                String[] lines = block.trim().split("\\r?\\n");
                
                if (lines.length >= 2) {
                    // Last line is expected output, everything else is input
                    StringBuilder inputBuilder = new StringBuilder();
                    for (int i = 0; i < lines.length - 1; i++) {
                        if (inputBuilder.length() > 0) inputBuilder.append("\n");
                        inputBuilder.append(lines[i].trim());
                    }
                    String expectedOutput = lines[lines.length - 1].trim();
                    
                    if (!inputBuilder.toString().isEmpty() && !expectedOutput.isEmpty()) {
                        testCases.add(new TestCase(inputBuilder.toString(), expectedOutput, testCases.size() < 2));
                        log.debug("Added test case {}: input='{}', expected='{}'", 
                                 testCases.size(), inputBuilder.toString().replace("\n", "\\n"), expectedOutput);
                    }
                }
            }
            
            if (!testCases.isEmpty()) {
                log.info("Successfully parsed {} test cases from simple line format", (Object)testCases.size());
                return testCases;
            }
            
            // If regex didn't work, fall back to manual parsing
            String[] segments = new String[]{content};
            
            for (int i = 0; i < segments.length; i++) {
                String segment = segments[i].trim();
                log.debug("Processing segment {}: '{}'", i, segment);
                
                if (segment.isEmpty()) continue;
                
                // Pattern: numbers EXPECTED: output
                if (segment.contains("EXPECTED:")) {
                    String[] part = segment.split("EXPECTED:");
                    if (part.length == 2) {
                        String inputPart = part[0].trim();
                        String expectedOutput = part[1].trim();
                        
                        log.debug("Input part: '{}', Expected: '{}'", inputPart, expectedOutput);
                        
                        // For tree problems, preserve the exact input format (including null values)
                        // Handle array format: [1,2,null,3] -> 1 2 null 3
                        String inputStr = inputPart;
                        if (inputPart.startsWith("[") && inputPart.endsWith("]")) {
                            // Remove brackets and convert to space-separated, ensuring null values are preserved as strings
                            inputStr = inputPart.substring(1, inputPart.length() - 1)
                                              .replace(",", " ")
                                              .trim();
                        } else {
                            // Already space-separated or other format
                            inputStr = inputPart.replace(",", " ").trim();
                        }
                        
                        log.debug("Processed input string: '{}'", inputStr);
                        
                        log.debug("Converted input: '{}'", inputStr);
                        
                        testCases.add(new TestCase(inputStr, expectedOutput, testCases.size() < 2));
                        log.debug("Parsed simple line test case {}: input='{}', expected='{}'", 
                                 testCases.size(), inputStr, expectedOutput);
                    }
                }
            }
            
            log.info("Successfully parsed {} test cases from simple line format", (Object)testCases.size());
        } catch (Exception e) {
            log.error("Error parsing simple line format: {}", (Object)e.getMessage(), (Object)e);
        }
        return testCases;
    }
    
    private List<TestCase> parseInputOutputFormat(String content) {
        List<TestCase> testCases = new ArrayList<TestCase>();
        try {
            log.info("Attempting to parse INPUT/OUTPUT format");
            
            // Split by INPUT: to get test cases
            String[] parts = content.split("INPUT:");
            
            for (String part : parts) {
                if (part.trim().isEmpty()) continue;
                
                // Split by OUTPUT: to separate input and expected output
                if (part.contains("OUTPUT:")) {
                    String[] inputOutput = part.split("OUTPUT:");
                    if (inputOutput.length == 2) {
                        String inputSection = inputOutput[0].trim();
                        String outputSection = inputOutput[1].trim();
                        
                        // Clean up input section - remove any remaining labels and get just the data
                        String[] inputLines = inputSection.split("\\r?\\n");
                        StringBuilder cleanInput = new StringBuilder();
                        
                        for (String line : inputLines) {
                            line = line.trim();
                            if (!line.isEmpty() && !line.equals("INPUT:") && !line.equals("OUTPUT:")) {
                                if (cleanInput.length() > 0) cleanInput.append("\n");
                                cleanInput.append(line);
                            }
                        }
                        
                        // Clean up output section - get just the expected output
                        String[] outputLines = outputSection.split("\\r?\\n");
                        String expectedOutput = "";
                        
                        for (String line : outputLines) {
                            line = line.trim();
                            if (!line.isEmpty() && !line.equals("INPUT:") && !line.equals("OUTPUT:")) {
                                expectedOutput = line;
                                break; // Take first non-empty line as expected output
                            }
                        }
                        
                        if (!cleanInput.toString().isEmpty() && !expectedOutput.isEmpty()) {
                            testCases.add(new TestCase(cleanInput.toString(), expectedOutput, testCases.size() < 2));
                            log.debug("Parsed INPUT/OUTPUT test case {}: input='{}', expected='{}'", 
                                     testCases.size(), cleanInput.toString().replace("\n", "\\n"), expectedOutput);
                        }
                    }
                }
            }
            
            log.info("Successfully parsed {} test cases from INPUT/OUTPUT format", (Object)testCases.size());
        } catch (Exception e) {
            log.error("Error parsing INPUT/OUTPUT format: {}", (Object)e.getMessage(), (Object)e);
        }
        return testCases;
    }

    private List<TestCase> parseFallbackFormats(String content) {
        ArrayList<TestCase> testCases = new ArrayList<TestCase>();
        try {
            Pattern pattern = Pattern.compile("([^\\[]+?)\\s+(\\[[^\\]]+\\])");
            Matcher matcher = pattern.matcher(content);
            int testNumber = 0;
            while (matcher.find()) {
                String parameters = matcher.group(1).trim();
                String expectedOutput = matcher.group(2).trim();
                testCases.add(new TestCase(parameters, expectedOutput, testNumber < 2));
                log.debug("Parsed fallback test case {}: input='{}', expected='{}'", new Object[]{++testNumber, parameters, expectedOutput});
            }
            if (!testCases.isEmpty()) {
                log.info("Successfully parsed {} test cases from fallback format", (Object)testCases.size());
                return testCases;
            }
        }
        catch (Exception e) {
            log.debug("Fallback format 1 failed: {}", (Object)e.getMessage());
        }
        return this.parseOldFormat(content);
    }

    private List<TestCase> parseOldFormat(String content) {
        ArrayList<TestCase> testCases = new ArrayList<TestCase>();
        try {
            Pattern pattern = Pattern.compile("\\[([^\\]]+)\\](-?\\d+)\\[([^\\]]+)\\]");
            Matcher matcher = pattern.matcher(content);
            int testNumber = 0;
            while (matcher.find()) {
                String nums = "[" + matcher.group(1) + "]";
                String target = matcher.group(2).trim();
                String expectedOutput = "[" + matcher.group(3) + "]";
                String input = "nums = " + nums + ", target = " + target;
                testCases.add(new TestCase(input, expectedOutput, testNumber < 2));
                log.debug("Parsed old format test case {}: input='{}', expected='{}'", new Object[]{++testNumber, input, expectedOutput});
            }
            log.info("Successfully parsed {} test cases from old format", (Object)testCases.size());
        }
        catch (Exception e) {
            log.error("Error parsing old format: {}", (Object)e.getMessage(), (Object)e);
        }
        return testCases;
    }

    private List<TestCase> parseAlternativeFormat(String content) {
        ArrayList<TestCase> testCases = new ArrayList<TestCase>();
        try {
            String[] parts = content.split("\\s+");
            StringBuilder currentGroup = new StringBuilder();
            for (String part : parts) {
                currentGroup.append(part);
                Pattern pattern = Pattern.compile("\\[([^\\]]+)\\](-?\\d+)\\[([^\\]]+)\\]");
                Matcher matcher = pattern.matcher(currentGroup.toString());
                if (!matcher.find()) continue;
                String nums = "[" + matcher.group(1) + "]";
                String target = matcher.group(2).trim();
                String expectedOutput = "[" + matcher.group(3) + "]";
                String input = "nums = " + nums + ", target = " + target;
                testCases.add(new TestCase(input, expectedOutput, testCases.size() < 2));
                log.debug("Alternative parsed test case {}: input='{}', expected='{}'", new Object[]{testCases.size(), input, expectedOutput});
                currentGroup = new StringBuilder();
            }
            log.info("Alternative parsing found {} test cases", (Object)testCases.size());
        }
        catch (Exception e) {
            log.error("Error in alternative parsing: {}", (Object)e.getMessage(), (Object)e);
        }
        return testCases;
    }

    public List<TestCase> getTestCases(Long problemId, boolean includeHidden) {
        return this.getTestCases(problemId.toString(), includeHidden);
    }

    private String getFileContent(String objectName) throws Exception {
        try (GetObjectResponse inputStream = this.minioClient.getObject((GetObjectArgs)((GetObjectArgs.Builder)((GetObjectArgs.Builder)GetObjectArgs.builder().bucket(this.testCasesBucket)).object(objectName)).build());){
            String string = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return string;
        }
    }

    public static class TestCase {
        private final String input;
        private final String expectedOutput;
        private final boolean visible;

        public TestCase(String input, String expectedOutput, boolean visible) {
            this.input = input;
            this.expectedOutput = expectedOutput;
            this.visible = visible;
        }

        public String getInput() {
            return this.input;
        }

        public String getExpectedOutput() {
            return this.expectedOutput;
        }

        public boolean isVisible() {
            return this.visible;
        }
    }
}
