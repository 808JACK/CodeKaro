/*
 * Decompiled with CFR 0.152.
 */
package com.example.demo.utils;

import com.example.demo.dtos.TextOperation;
import java.util.Arrays;
import java.util.List;

public class OtUtils {
    public static String apply(String doc, TextOperation operation) throws IllegalArgumentException {
        StringBuilder newDoc = new StringBuilder();
        int docIndex = 0;
        for (Object op : operation.getOps()) {
            if (TextOperation.isRetain(op)) {
                int retainCount = (Integer)op;
                if (docIndex + retainCount > doc.length()) {
                    throw new IllegalArgumentException("Retain exceeds document length.");
                }
                newDoc.append(doc, docIndex, docIndex + retainCount);
                docIndex += retainCount;
                continue;
            }
            if (TextOperation.isInsert(op)) {
                newDoc.append((String)op);
                continue;
            }
            if (TextOperation.isDelete(op)) {
                int deleteCount = -((Integer)op).intValue();
                if (docIndex + deleteCount > doc.length()) {
                    throw new IllegalArgumentException("Delete exceeds document length.");
                }
                docIndex += deleteCount;
                continue;
            }
            throw new IllegalArgumentException("Invalid op type in operation: " + String.valueOf(op));
        }
        if (docIndex != doc.length()) {
            throw new IllegalArgumentException("Operation did not consume the entire document.");
        }
        return newDoc.toString();
    }

    public static TextOperation invert(String doc, TextOperation operation) {
        TextOperation inverse = new TextOperation();
        int docIndex = 0;
        for (Object op : operation.getOps()) {
            if (TextOperation.isRetain(op)) {
                int retainCount = (Integer)op;
                inverse.retain(retainCount);
                docIndex += retainCount;
                continue;
            }
            if (TextOperation.isInsert(op)) {
                inverse.delete(((String)op).length());
                continue;
            }
            if (TextOperation.isDelete(op)) {
                int deleteCount = -((Integer)op).intValue();
                inverse.insert(doc.substring(docIndex, docIndex + deleteCount));
                docIndex += deleteCount;
                continue;
            }
            throw new IllegalStateException("Invalid op type during invert: " + String.valueOf(op));
        }
        return inverse;
    }

    public static TextOperation compose(TextOperation op1, TextOperation op2) throws IllegalArgumentException {
        Object currentOp2;
        if (op1.getTargetLength() != op2.getBaseLength()) {
            throw new IllegalArgumentException("Compose error: op1 target length (" + op1.getTargetLength() + ") must match op2 base length (" + op2.getBaseLength() + ").");
        }
        TextOperation composed = new TextOperation();
        List<Object> ops1 = op1.getOps();
        List<Object> ops2 = op2.getOps();
        int i1 = 0;
        int i2 = 0;
        Object currentOp1 = i1 < ops1.size() ? ops1.get(i1++) : null;
        Object object = currentOp2 = i2 < ops2.size() ? ops2.get(i2++) : null;
        while (currentOp1 != null || currentOp2 != null) {
            int deleteCount;
            if (TextOperation.isDelete(currentOp1)) {
                composed.delete((Integer)currentOp1);
                currentOp1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                continue;
            }
            if (TextOperation.isInsert(currentOp2)) {
                composed.insert((String)currentOp2);
                currentOp2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                continue;
            }
            if (currentOp1 == null) {
                throw new IllegalArgumentException("Cannot compose: op2 is longer than op1 affects.");
            }
            if (currentOp2 == null) {
                throw new IllegalArgumentException("Cannot compose: op1 is longer than op2 affects.");
            }
            if (TextOperation.isRetain(currentOp1) && TextOperation.isRetain(currentOp2)) {
                int retain2;
                int retain1 = (Integer)currentOp1;
                if (retain1 > (retain2 = ((Integer)currentOp2).intValue())) {
                    composed.retain(retain2);
                    currentOp1 = retain1 - retain2;
                    currentOp2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                    continue;
                }
                if (retain1 == retain2) {
                    composed.retain(retain1);
                    currentOp1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                    currentOp2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                    continue;
                }
                composed.retain(retain1);
                currentOp2 = retain2 - retain1;
                currentOp1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                continue;
            }
            if (TextOperation.isInsert(currentOp1) && TextOperation.isDelete(currentOp2)) {
                String insertStr = (String)currentOp1;
                deleteCount = (Integer)currentOp2;
                if (insertStr.length() > -deleteCount) {
                    currentOp1 = insertStr.substring(0, insertStr.length() + deleteCount);
                    currentOp2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                    continue;
                }
                if (insertStr.length() == -deleteCount) {
                    currentOp1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                    currentOp2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                    continue;
                }
                currentOp2 = deleteCount + insertStr.length();
                currentOp1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                continue;
            }
            if (TextOperation.isInsert(currentOp1) && TextOperation.isRetain(currentOp2)) {
                String insertStr = (String)currentOp1;
                int retainCount = (Integer)currentOp2;
                if (insertStr.length() > retainCount) {
                    composed.insert(insertStr.substring(0, retainCount));
                    currentOp1 = insertStr.substring(retainCount);
                    currentOp2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                    continue;
                }
                if (insertStr.length() == retainCount) {
                    composed.insert(insertStr);
                    currentOp1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                    currentOp2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                    continue;
                }
                composed.insert(insertStr);
                currentOp2 = retainCount - insertStr.length();
                currentOp1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                continue;
            }
            if (TextOperation.isRetain(currentOp1) && TextOperation.isDelete(currentOp2)) {
                int retainCount = (Integer)currentOp1;
                if (retainCount > -(deleteCount = ((Integer)currentOp2).intValue())) {
                    composed.delete(deleteCount);
                    currentOp1 = retainCount + deleteCount;
                    currentOp2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                    continue;
                }
                if (retainCount == -deleteCount) {
                    composed.delete(deleteCount);
                    currentOp1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                    currentOp2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                    continue;
                }
                composed.delete(-retainCount);
                currentOp2 = deleteCount + retainCount;
                currentOp1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                continue;
            }
            throw new IllegalStateException("Unhandled case in compose: op1=" + String.valueOf(currentOp1) + ", op2=" + String.valueOf(currentOp2));
        }
        return composed;
    }

    public static List<TextOperation> transform(TextOperation operation1, TextOperation operation2) throws IllegalArgumentException {
        Object op2;
        if (operation1.getBaseLength() != operation2.getBaseLength()) {
            throw new IllegalArgumentException(String.format("Both operations have to have the same base length (op1: %d, op2: %d)", operation1.getBaseLength(), operation2.getBaseLength()));
        }
        TextOperation operation1prime = new TextOperation();
        TextOperation operation2prime = new TextOperation();
        List<Object> ops1 = operation1.getOps();
        List<Object> ops2 = operation2.getOps();
        int i1 = 0;
        int i2 = 0;
        Object op1 = i1 < ops1.size() ? ops1.get(i1++) : null;
        Object object = op2 = i2 < ops2.size() ? ops2.get(i2++) : null;
        while (!(op1 == null && op2 == null || op1 == null && op2 == null)) {
            int op2Delete;
            int op1Delete;
            int minLength;
            int op2Retain;
            int op1Retain;
            if (TextOperation.isInsert(op1)) {
                operation1prime.insert((String)op1);
                operation2prime.retain(((String)op1).length());
                op1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                continue;
            }
            if (TextOperation.isInsert(op2)) {
                operation1prime.retain(((String)op2).length());
                operation2prime.insert((String)op2);
                op2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                continue;
            }
            if (op1 == null) {
                throw new IllegalArgumentException("Cannot transform operations: first operation is too short.");
            }
            if (op2 == null) {
                throw new IllegalArgumentException("Cannot transform operations: second operation is too short.");
            }
            if (TextOperation.isRetain(op1) && TextOperation.isRetain(op2)) {
                op1Retain = (Integer)op1;
                if (op1Retain > (op2Retain = ((Integer)op2).intValue())) {
                    minLength = op2Retain;
                    op1 = op1Retain - op2Retain;
                    op2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                } else if (op1Retain == op2Retain) {
                    minLength = op2Retain;
                    op1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                    op2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                } else {
                    minLength = op1Retain;
                    op2 = op2Retain - op1Retain;
                    op1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                }
                operation1prime.retain(minLength);
                operation2prime.retain(minLength);
                continue;
            }
            if (TextOperation.isDelete(op1) && TextOperation.isDelete(op2)) {
                op1Delete = (Integer)op1;
                if (-op1Delete > -(op2Delete = ((Integer)op2).intValue())) {
                    op1 = op1Delete - op2Delete;
                    op2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                    continue;
                }
                if (-op1Delete == -op2Delete) {
                    op1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                    op2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                    continue;
                }
                op2 = op2Delete - op1Delete;
                op1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                continue;
            }
            if (TextOperation.isDelete(op1) && TextOperation.isRetain(op2)) {
                op1Delete = (Integer)op1;
                if (-op1Delete > (op2Retain = ((Integer)op2).intValue())) {
                    minLength = op2Retain;
                    op1 = op1Delete + op2Retain;
                    op2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                } else if (-op1Delete == op2Retain) {
                    minLength = op2Retain;
                    op1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                    op2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                } else {
                    minLength = -op1Delete;
                    op2 = op2Retain + op1Delete;
                    op1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                }
                operation1prime.delete(minLength);
                continue;
            }
            if (TextOperation.isRetain(op1) && TextOperation.isDelete(op2)) {
                op1Retain = (Integer)op1;
                if (op1Retain > -(op2Delete = ((Integer)op2).intValue())) {
                    minLength = -op2Delete;
                    op1 = op1Retain + op2Delete;
                    op2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                } else if (op1Retain == -op2Delete) {
                    minLength = op1Retain;
                    op1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                    op2 = i2 < ops2.size() ? ops2.get(i2++) : null;
                } else {
                    minLength = op1Retain;
                    op2 = op2Delete + op1Retain;
                    op1 = i1 < ops1.size() ? ops1.get(i1++) : null;
                }
                operation2prime.delete(minLength);
                continue;
            }
            throw new IllegalStateException("Unrecognized case in transform: op1=" + String.valueOf(op1) + ", op2=" + String.valueOf(op2));
        }
        return Arrays.asList(operation1prime, operation2prime);
    }
}
