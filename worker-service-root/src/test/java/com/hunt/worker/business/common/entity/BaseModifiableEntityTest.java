package com.hunt.worker-service-root.business.common.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BaseModifiableEntityTest {

    @Test
    public void preUpdate() {
        //GIVEN
        BaseModifiableEntity<Long> baseModifiableEntity = new BaseModifiableEntity<Long>() {
            @Override
            public Long getId() {
                return 123L;
            }
        };

        //WHEN
        baseModifiableEntity.fillModified();

        //THEN
        assertNotNull(baseModifiableEntity.getModified());
    }

}