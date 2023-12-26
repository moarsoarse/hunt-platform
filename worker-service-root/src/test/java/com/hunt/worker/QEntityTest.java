package com.hunt.worker-service-root;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.text.MessageFormat;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;

public class QEntityTest {

    private static final String Q_CLASS_ERROR_MESSAGE = "Class {0} should not start with \"Q\" which is reserved prefix for QueryDSL classes. \"Q\" prefixed classes break code coverage and we exclude them.";

    @Test
    public void testEntityNameStartsWithQ() {
        //WHEN
        Reflections reflections = new Reflections("com.hunt.worker-service-root");

        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class, true);
        entityClasses.addAll(reflections.getTypesAnnotatedWith(MappedSuperclass.class));

        for (Class<?> entityClass : entityClasses) {
            if (entityClass.getSimpleName().startsWith("Q")) {
                fail(MessageFormat.format(Q_CLASS_ERROR_MESSAGE, entityClass.getName()));
            }
        }
    }
}
