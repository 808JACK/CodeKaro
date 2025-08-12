/*
 * Decompiled with CFR 0.152.
 */
package com.example.demo.repos;

import com.example.demo.entities.OTOperation;
import java.util.List;

public interface OTOperationRepository {
    public List<OTOperation> findByRoomIdOrderByOperationId(Long var1);

    public List<OTOperation> findByRoomId(Long var1);

    public List<OTOperation> findByRoomIdAndVersionGreaterThan(Long var1, Integer var2);

    public long countByRoomId(Long var1);

    public List<OTOperation> findByRoomIdOrderByTimestampDesc(Long var1);

    public void deleteByRoomId(Long var1);

    public OTOperation save(OTOperation var1);
}
