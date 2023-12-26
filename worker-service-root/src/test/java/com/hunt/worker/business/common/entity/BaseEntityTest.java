package com.hunt.worker-service-root.business.common.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BaseEntityTest {

    @Test
    public void prePersistWhenCreatedArbitrarySet() {
        //GIVEN
        BaseEntity<Long> baseEntity = new BaseEntity<>() {
            @Override
            public Long getId() {
                return null;
            }
        };
        baseEntity.created = LocalDateTime.MIN;

        //WHEN
        baseEntity.fillCreated();

        //THEN
        assertEquals(LocalDateTime.MIN, baseEntity.getCreated());
    }

    @Test
    public void prePersistWhenCreatedNotSet() {
        //GIVEN
        BaseEntity<Long> baseEntity = new BaseEntity<>() {
            @Override
            public Long getId() {
                return null;
            }
        };

        //WHEN
        baseEntity.fillCreated();

        //THEN
        assertNotNull(baseEntity.getCreated());
    }

    @Test
    public void isNewWhenNullId() {
        //GIVEN
        BaseEntity<Long> baseEntity = new BaseEntity<>() {
            @Override
            public Long getId() {
                return null;
            }
        };

        //WHEN
        boolean isNew = baseEntity.isNew();

        //THEN
        assertTrue(isNew);
    }

    @Test
    public void isNewWhenNotNullId() {
        //GIVEN
        BaseEntity<Long> baseEntity = new BaseEntity<>() {
            @Override
            public Long getId() {
                return 123L;
            }
        };

        //WHEN
        boolean isNew = baseEntity.isNew();

        //THEN
        assertFalse(isNew);
    }
}