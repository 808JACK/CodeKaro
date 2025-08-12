/*
 * Decompiled with CFR 0.152.
 */
package com.example.demo.services;

import com.example.demo.dtos.OTOperationDTO;
import com.example.demo.entities.OTOperation;
import java.util.List;

public interface OTServiceInterface {
    public OTOperation saveOperation(OTOperationDTO var1, String var2);

    public OTOperation processOperation(String var1, Long var2, OTOperationDTO var3, Integer var4);

    public List<OTOperation> getOperationsForRoom(String var1);

    public List<OTOperation> getOperationsAfterVersion(String var1, Integer var2);

    public long getOperationCount(String var1);

    public String reconstructCurrentCode(String var1, String var2);

    public OTOperationDTO convertToDTO(OTOperation var1, String var2);
}
