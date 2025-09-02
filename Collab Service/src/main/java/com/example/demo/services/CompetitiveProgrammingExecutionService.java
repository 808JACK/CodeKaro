package com.example.demo.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CompetitiveProgrammingExecutionService {
    
    /**
     * Convert competitive programming input to proper function call format
     * Handles different problem types automatically
     */
    public String convertInputToFunctionCall(String rawInput, String problemName, String userCode) {
        try {
            log.info("Converting input for problem: {}", problemName);
            log.debug("Raw input: '{}'", rawInput);
            
            String problemType = detectProblemType(problemName, rawInput, userCode);
            log.info("Detected problem type: {}", problemType);
            
            switch (problemType.toLowerCase()) {
                case "twosum":
                    return convertTwoSumInput(rawInput);
                case "threesum":
                    return convertThreeSumInput(rawInput);
                case "linkedlist":
                    return convertLinkedListInput(rawInput);
                case "tree":
                    return convertTreeInput(rawInput);
                case "array":
                    return convertArrayInput(rawInput);
                default:
                    return convertGenericInput(rawInput);
            }
            
        } catch (Exception e) {
            log.error("Error converting input for problem {}: {}", problemName, e.getMessage());
            return rawInput; // Return original if conversion fails
        }
    }
    
    /**
     * Detect problem type from problem name, input pattern, and user code
     */
    private String detectProblemType(String problemName, String input, String userCode) {
        String name = problemName.toLowerCase();
        String code = userCode.toLowerCase();
        
        // Check problem name first
        if (name.contains("two") && name.contains("sum")) return "twosum";
        if (name.contains("three") && name.contains("sum")) return "threesum";
        if (name.contains("linked") && name.contains("list")) return "linkedlist";
        if (name.contains("tree") || name.contains("binary")) return "tree";
        
        // Check function signature in user code
        if (code.contains("def twosum") || code.contains("def two_sum")) return "twosum";
        if (code.contains("def threesum") || code.contains("def three_sum")) return "threesum";
        if (code.contains("listnode") || code.contains("def reverse") || code.contains("linked")) return "linkedlist";
        if (code.contains("treenode") || code.contains("def inorder") || code.contains("def preorder") || 
            code.contains("def postorder") || code.contains("binary") || code.contains("tree")) return "tree";
        
        // Check input pattern
        String[] parts = input.trim().split("\\s+");
        if (parts.length >= 3) {
            // Multiple numbers - likely array + target problem
            return "twosum";
        }
        
        if (input.contains("null") || input.matches(".*\\d+\\s+null.*")) return "tree";
        
        return "array"; // Default to array processing
    }
    
    /**
     * Convert Two Sum input: "2 7 11 159" -> nums=[2,7,11,15], target=9
     * Handles cases where numbers might be concatenated
     */
    private String convertTwoSumInput(String input) {
        try {
            log.debug("Converting TwoSum input: '{}'", input);
            
            // First try normal space-separated parsing
            String[] parts = input.trim().split("\\s+");
            
            if (parts.length >= 2) {
                // Normal case: space-separated numbers
                StringBuilder nums = new StringBuilder();
                for (int i = 0; i < parts.length - 1; i++) {
                    if (i > 0) nums.append(" ");
                    nums.append(parts[i]);
                }
                String target = parts[parts.length - 1];
                
                log.debug("Normal parsing: nums='{}', target='{}'", nums.toString(), target);
                return nums.toString() + "\n" + target;
            }
            
            // Handle concatenated numbers case like "2711159" -> "2 7 11 15" + "9"
            if (parts.length == 1 && parts[0].matches(".*\\d.*")) {
                String numberString = parts[0];
                
                // Try to intelligently split the numbers
                // For TwoSum, we typically expect 3-5 numbers total
                List<Integer> numbers = parseNumberString(numberString);
                
                if (numbers.size() >= 2) {
                    StringBuilder nums = new StringBuilder();
                    for (int i = 0; i < numbers.size() - 1; i++) {
                        if (i > 0) nums.append(" ");
                        nums.append(numbers.get(i));
                    }
                    String target = String.valueOf(numbers.get(numbers.size() - 1));
                    
                    log.debug("Concatenated parsing: nums='{}', target='{}'", nums.toString(), target);
                    return nums.toString() + "\n" + target;
                }
            }
            
            // Fallback: return as-is
            log.debug("Fallback: returning input as-is");
            return input;
            
        } catch (Exception e) {
            log.error("Error converting TwoSum input '{}': {}", input, e.getMessage());
            return input;
        }
    }
    
    /**
     * Parse a concatenated number string into individual numbers
     * Handles cases like "2711159" or "-1-2-3-4-5-8"
     */
    private List<Integer> parseNumberString(String numberString) {
        List<Integer> numbers = new ArrayList<>();
        
        try {
            // Handle negative numbers: "-1-2-3-4-5-8" -> [-1, -2, -3, -4, -5, -8]
            if (numberString.contains("-") && !numberString.startsWith("-")) {
                // Split on negative signs, but keep the negative sign
                String[] parts = numberString.split("(?=-)");
                for (String part : parts) {
                    if (!part.isEmpty()) {
                        numbers.add(Integer.parseInt(part));
                    }
                }
                return numbers;
            }
            
            // Handle positive numbers: try different splitting strategies
            // Strategy 1: Assume single digits "2711159" -> [2,7,1,1,1,5,9]
            if (numberString.matches("\\d+") && numberString.length() > 2) {
                // Try to find a reasonable split
                // For TwoSum, common patterns are 3-5 numbers
                
                // Try splitting into reasonable chunks
                if (numberString.length() <= 6) {
                    // Short string: try single digits first
                    for (char c : numberString.toCharArray()) {
                        numbers.add(Character.getNumericValue(c));
                    }
                } else {
                    // Longer string: try to find patterns
                    // This is heuristic - in real scenarios, you'd need better logic
                    int i = 0;
                    while (i < numberString.length()) {
                        if (i + 1 < numberString.length()) {
                            // Try 2-digit numbers first
                            String twoDigit = numberString.substring(i, i + 2);
                            int num = Integer.parseInt(twoDigit);
                            if (num >= 10 && num <= 99) {
                                numbers.add(num);
                                i += 2;
                                continue;
                            }
                        }
                        // Fall back to single digit
                        numbers.add(Character.getNumericValue(numberString.charAt(i)));
                        i++;
                    }
                }
            }
            
        } catch (Exception e) {
            log.debug("Error parsing number string '{}': {}", numberString, e.getMessage());
        }
        
        return numbers;
    }
    
    /**
     * Convert Three Sum input: "nums = [-1,0,1,2,-1,-4]" -> array format
     */
    private String convertThreeSumInput(String input) {
        // For three sum, typically just the array
        return input.replace(",", " ").replace("[", "").replace("]", "").trim();
    }
    
    /**
     * Convert Linked List input: "1 2 3 4 5" -> space-separated values
     */
    private String convertLinkedListInput(String input) {
        return input.trim();
    }
    
    /**
     * Convert Tree input: "1 null 2 3" -> preserve null values
     */
    private String convertTreeInput(String input) {
        return input.replace(",", " ").replace("[", "").replace("]", "").trim();
    }
    
    /**
     * Convert Array input: general array processing
     */
    private String convertArrayInput(String input) {
        return input.replace(",", " ").replace("[", "").replace("]", "").trim();
    }
    
    /**
     * Generic input conversion
     */
    private String convertGenericInput(String input) {
        return input.trim();
    }
    
    /**
     * Generate Python test script for competitive programming problems
     */
    public String generateTestScript(String userCode, List<TestCase> testCases, String problemName) {
        StringBuilder script = new StringBuilder();
        
        // Add all necessary imports automatically
        script.append("# Automatic imports - users don't need to worry about these\n");
        script.append("import json\n");
        script.append("import sys\n");
        script.append("import math\n");
        script.append("import collections\n");
        script.append("from collections import defaultdict, deque, Counter\n");
        script.append("import heapq\n");
        script.append("import bisect\n");
        script.append("import itertools\n");
        script.append("from typing import List, Optional, Dict, Set, Tuple\n");
        script.append("import re\n");
        script.append("import functools\n");
        script.append("from functools import lru_cache\n\n");
        
        // Add user's code
        script.append("# User's solution\n");
        script.append(userCode).append("\n\n");
        
        // Detect function name from user code
        String functionName = detectFunctionName(userCode);
        
        // Simple approach: wrap user code in a function and provide input via stdin simulation
        script.append("import io\n");
        script.append("import sys\n\n");
        
        script.append("def run_tests():\n");
        script.append("    results = []\n");
        script.append("    \n");
        
        for (int i = 0; i < testCases.size(); i++) {
            TestCase tc = testCases.get(i);
            String convertedInput = convertInputToFunctionCall(tc.getInput(), problemName, userCode);
            String problemType = detectProblemType(problemName, tc.getInput(), userCode);
            
            script.append("    # Test case ").append(i + 1).append("\n");
            script.append("    try:\n");
            
            // For Two Sum problems, provide input via stdin simulation
            if ("twosum".equals(problemType)) {
                String[] inputLines = convertedInput.split("\n");
                if (inputLines.length >= 2) {
                    String[] nums = inputLines[0].trim().split("\\s+");
                    String target = inputLines[1].trim();
                    
                    script.append("        # Provide input for Two Sum problem\n");
                    script.append("        test_input = \"\"\"").append(nums.length).append("\\n");
                    script.append(inputLines[0]).append("\\n");
                    script.append(target).append("\\n\"\"\"\n");
                    script.append("        \n");
                    script.append("        # Simulate stdin and capture stdout\n");
                    script.append("        old_stdin = sys.stdin\n");
                    script.append("        old_stdout = sys.stdout\n");
                    script.append("        \n");
                    script.append("        sys.stdin = io.StringIO(test_input)\n");
                    script.append("        captured_output = io.StringIO()\n");
                    script.append("        sys.stdout = captured_output\n");
                    script.append("        \n");
                    script.append("        try:\n");
                    
                    // Execute the user's main code
                    String[] userCodeLines = userCode.split("\n");
                    for (String line : userCodeLines) {
                        String trimmedLine = line.trim();
                        if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                            continue;
                        }
                        if (trimmedLine.startsWith("import ") || trimmedLine.startsWith("from ")) {
                            continue;
                        }
                        if (trimmedLine.startsWith("def ")) {
                            continue;
                        }
                        script.append("            ").append(line).append("\n");
                    }
                    
                    script.append("        finally:\n");
                    script.append("            # Restore stdin and stdout\n");
                    script.append("            sys.stdin = old_stdin\n");
                    script.append("            sys.stdout = old_stdout\n");
                    script.append("        \n");
                    script.append("        # Get the output\n");
                    script.append("        result_output = captured_output.getvalue().strip()\n");
                    script.append("        result = [int(x) for x in result_output.split()] if result_output else []\n");
                }
            } else {
                // For other problems, use stdin simulation
                script.append("        test_input = \"\"\"").append(convertedInput).append("\\n\"\"\"\n");
                script.append("        \n");
                script.append("        # Simulate stdin and capture stdout\n");
                script.append("        old_stdin = sys.stdin\n");
                script.append("        old_stdout = sys.stdout\n");
                script.append("        \n");
                script.append("        sys.stdin = io.StringIO(test_input)\n");
                script.append("        captured_output = io.StringIO()\n");
                script.append("        sys.stdout = captured_output\n");
                script.append("        \n");
                script.append("        try:\n");
                script.append("            # Execute user's main code\n");
                
                // Extract the main execution part of user's code
                String[] userCodeLines = userCode.split("\n");
                boolean inMainCode = false;
                for (String line : userCodeLines) {
                    String trimmedLine = line.trim();
                    if (trimmedLine.isEmpty() || trimmedLine.startsWith("#")) {
                        continue;
                    }
                    if (trimmedLine.startsWith("import ") || trimmedLine.startsWith("from ")) {
                        continue;
                    }
                    if (trimmedLine.startsWith("def ")) {
                        inMainCode = false;
                        continue;
                    }
                    if (!inMainCode && !trimmedLine.startsWith("class ")) {
                        inMainCode = true;
                    }
                    if (inMainCode) {
                        script.append("            ").append(line).append("\n");
                    }
                }
                script.append("        finally:\n");
                script.append("            # Restore stdin and stdout\n");
                script.append("            sys.stdin = old_stdin\n");
                script.append("            sys.stdout = old_stdout\n");
                script.append("        \n");
                script.append("        # Get the output\n");
                script.append("        result_output = captured_output.getvalue().strip()\n");
                script.append("        result = [int(x) for x in result_output.split()] if result_output else []\n");
            }
            
            // Convert expected output to proper Python list format
            String expectedStr = tc.getExpectedOutput().trim().replaceAll("\\s+", ", ");
            script.append("        expected = [").append(expectedStr).append("]\n");
            script.append("        verdict = 'AC' if result == expected else 'WA'\n");
            script.append("        results.append({\n");
            script.append("            'test': ").append(i + 1).append(",\n");
            script.append("            'verdict': verdict,\n");
            script.append("            'output': str(result),\n");
            script.append("            'expected': str(expected),\n");
            script.append("            'error': ''\n");
            script.append("        })\n");
            script.append("    except Exception as e:\n");
            script.append("        # Restore stdin and stdout in case of error\n");
            script.append("        sys.stdin = old_stdin\n");
            script.append("        sys.stdout = old_stdout\n");
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
        
        // Add helper functions and common data structures
        script.append("# Helper functions and data structures - automatically provided\n");
        
        // ListNode for linked list problems
        script.append("class ListNode:\n");
        script.append("    def __init__(self, val=0, next=None):\n");
        script.append("        self.val = val\n");
        script.append("        self.next = next\n");
        script.append("    def __repr__(self):\n");
        script.append("        return f'ListNode({self.val})'\n\n");
        
        // TreeNode for tree problems
        script.append("class TreeNode:\n");
        script.append("    def __init__(self, val=0, left=None, right=None):\n");
        script.append("        self.val = val\n");
        script.append("        self.left = left\n");
        script.append("        self.right = right\n");
        script.append("    def __repr__(self):\n");
        script.append("        return f'TreeNode({self.val})'\n\n");
        
        // Union-Find for graph problems
        script.append("class UnionFind:\n");
        script.append("    def __init__(self, n):\n");
        script.append("        self.parent = list(range(n))\n");
        script.append("        self.rank = [0] * n\n");
        script.append("    def find(self, x):\n");
        script.append("        if self.parent[x] != x:\n");
        script.append("            self.parent[x] = self.find(self.parent[x])\n");
        script.append("        return self.parent[x]\n");
        script.append("    def union(self, x, y):\n");
        script.append("        px, py = self.find(x), self.find(y)\n");
        script.append("        if px == py: return False\n");
        script.append("        if self.rank[px] < self.rank[py]: px, py = py, px\n");
        script.append("        self.parent[py] = px\n");
        script.append("        if self.rank[px] == self.rank[py]: self.rank[px] += 1\n");
        script.append("        return True\n\n");
        
        script.append("def create_linked_list(values):\n");
        script.append("    if not values: return None\n");
        script.append("    head = ListNode(values[0])\n");
        script.append("    current = head\n");
        script.append("    for val in values[1:]:\n");
        script.append("        current.next = ListNode(val)\n");
        script.append("        current = current.next\n");
        script.append("    return head\n\n");
        
        script.append("def linked_list_to_array(head):\n");
        script.append("    result = []\n");
        script.append("    current = head\n");
        script.append("    while current:\n");
        script.append("        result.append(current.val)\n");
        script.append("        current = current.next\n");
        script.append("    return result\n\n");
        
        // Tree helper functions
        script.append("def create_tree_from_array(arr):\n");
        script.append("    if not arr or arr[0] is None: return None\n");
        script.append("    root = TreeNode(arr[0])\n");
        script.append("    queue = [root]\n");
        script.append("    i = 1\n");
        script.append("    while queue and i < len(arr):\n");
        script.append("        node = queue.pop(0)\n");
        script.append("        if i < len(arr) and arr[i] is not None:\n");
        script.append("            node.left = TreeNode(arr[i])\n");
        script.append("            queue.append(node.left)\n");
        script.append("        i += 1\n");
        script.append("        if i < len(arr) and arr[i] is not None:\n");
        script.append("            node.right = TreeNode(arr[i])\n");
        script.append("            queue.append(node.right)\n");
        script.append("        i += 1\n");
        script.append("    return root\n\n");
        
        script.append("def tree_to_array(root):\n");
        script.append("    if not root: return []\n");
        script.append("    result, queue = [], [root]\n");
        script.append("    while queue:\n");
        script.append("        node = queue.pop(0)\n");
        script.append("        if node:\n");
        script.append("            result.append(node.val)\n");
        script.append("            queue.extend([node.left, node.right])\n");
        script.append("        else:\n");
        script.append("            result.append(None)\n");
        script.append("    # Remove trailing None values\n");
        script.append("    while result and result[-1] is None:\n");
        script.append("        result.pop()\n");
        script.append("    return result\n\n");
        
        script.append("if __name__ == '__main__':\n");
        script.append("    try:\n");
        script.append("        test_results = run_tests()\n");
        script.append("        print(json.dumps(test_results))\n");
        script.append("    except Exception as e:\n");
        script.append("        print(json.dumps([{'test': 1, 'verdict': 'CE', 'output': '', 'expected': '', 'error': str(e)}]))\n");
        
        return script.toString();
    }
    
    /**
     * Detect function name from user code
     */
    private String detectFunctionName(String code) {
        String[] lines = code.split("\n");
        String lastFunctionName = null;
        
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("def ") && line.contains("(")) {
                int parenIndex = line.indexOf("(");
                String funcDef = line.substring(4, parenIndex);
                String funcName = funcDef.trim();
                
                if (!funcName.startsWith("_")) { // Skip private functions
                    lastFunctionName = funcName;
                }
            }
        }
        
        if (lastFunctionName != null) {
            log.debug("Detected function name: {}", lastFunctionName);
            return lastFunctionName;
        }
        
        log.warn("Could not detect function name, using default 'solution'");
        return "solution";
    }
    
    public static class TestCase {
        private final String input;
        private final String expectedOutput;
        
        public TestCase(String input, String expectedOutput) {
            this.input = input;
            this.expectedOutput = expectedOutput;
        }
        
        public String getInput() { return input; }
        public String getExpectedOutput() { return expectedOutput; }
    }
}