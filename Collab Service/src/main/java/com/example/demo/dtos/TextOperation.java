/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public class TextOperation {
    private List<Object> ops = new ArrayList<Object>();
    private int baseLength = 0;
    private int targetLength = 0;

    public TextOperation(List<Object> ops) {
        this.ops = new ArrayList<Object>(ops);
        this.calculateLengths();
    }

    public TextOperation retain(int n) {
        if (n <= 0) {
            return this;
        }
        this.baseLength += n;
        this.targetLength += n;
        if (!this.ops.isEmpty() && TextOperation.isRetain(this.ops.get(this.ops.size() - 1))) {
            int lastRetain = (Integer)this.ops.get(this.ops.size() - 1);
            this.ops.set(this.ops.size() - 1, lastRetain + n);
        } else {
            this.ops.add(n);
        }
        return this;
    }

    public TextOperation insert(String str) {
        if (str == null || str.isEmpty()) {
            return this;
        }
        this.targetLength += str.length();
        if (!this.ops.isEmpty() && TextOperation.isInsert(this.ops.get(this.ops.size() - 1))) {
            String lastInsert = (String)this.ops.get(this.ops.size() - 1);
            this.ops.set(this.ops.size() - 1, lastInsert + str);
        } else {
            this.ops.add(str);
        }
        return this;
    }

    public TextOperation delete(int n) {
        if (n <= 0) {
            return this;
        }
        this.baseLength += n;
        if (!this.ops.isEmpty() && TextOperation.isDelete(this.ops.get(this.ops.size() - 1))) {
            int lastDelete = (Integer)this.ops.get(this.ops.size() - 1);
            this.ops.set(this.ops.size() - 1, lastDelete - n);
        } else {
            this.ops.add(-n);
        }
        return this;
    }

    public TextOperation delete(String str) {
        return this.delete(str.length());
    }

    public static boolean isRetain(Object op) {
        return op instanceof Integer && (Integer)op > 0;
    }

    public static boolean isInsert(Object op) {
        return op instanceof String;
    }

    public static boolean isDelete(Object op) {
        return op instanceof Integer && (Integer)op < 0;
    }

    private void calculateLengths() {
        this.baseLength = 0;
        this.targetLength = 0;
        for (Object op : this.ops) {
            int n;
            if (TextOperation.isRetain(op)) {
                n = (Integer)op;
                this.baseLength += n;
                this.targetLength += n;
                continue;
            }
            if (TextOperation.isInsert(op)) {
                String str = (String)op;
                this.targetLength += str.length();
                continue;
            }
            if (!TextOperation.isDelete(op)) continue;
            n = -((Integer)op).intValue();
            this.baseLength += n;
        }
    }

    public boolean isNoop() {
        return this.ops.size() == 1 && TextOperation.isRetain(this.ops.get(0)) && (Integer)this.ops.get(0) == this.baseLength;
    }

    public List<Object> getOps() {
        return new ArrayList<Object>(this.ops);
    }

    public String toString() {
        return "TextOperation{ops=" + String.valueOf(this.ops) + ", baseLength=" + this.baseLength + ", targetLength=" + this.targetLength + "}";
    }

    @Generated
    public int getBaseLength() {
        return this.baseLength;
    }

    @Generated
    public int getTargetLength() {
        return this.targetLength;
    }

    @Generated
    public void setOps(List<Object> ops) {
        this.ops = ops;
    }

    @Generated
    public void setBaseLength(int baseLength) {
        this.baseLength = baseLength;
    }

    @Generated
    public void setTargetLength(int targetLength) {
        this.targetLength = targetLength;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TextOperation)) {
            return false;
        }
        TextOperation other = (TextOperation)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getBaseLength() != other.getBaseLength()) {
            return false;
        }
        if (this.getTargetLength() != other.getTargetLength()) {
            return false;
        }
        List<Object> this$ops = this.getOps();
        List<Object> other$ops = other.getOps();
        return !(this$ops == null ? other$ops != null : !((Object)this$ops).equals(other$ops));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof TextOperation;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getBaseLength();
        result = result * 59 + this.getTargetLength();
        List<Object> $ops = this.getOps();
        result = result * 59 + ($ops == null ? 43 : ((Object)$ops).hashCode());
        return result;
    }

    @Generated
    public TextOperation() {
    }
}
