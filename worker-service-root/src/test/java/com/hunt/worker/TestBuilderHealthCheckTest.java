package com.hunt.worker-service-root;

import java.text.MessageFormat;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.springframework.stereotype.Repository;

import static org.junit.jupiter.api.Assertions.fail;

public class TestBuilderHealthCheckTest {

    private static final String NO_REPOSITORY_ANNOTATION_MESSAGE = "No @Repository annotation is present for = {0}.";

    @Test
    public void testAllDatabaseTestBuildersAsRepository() {
        Reflections reflactions = new Reflections();
        Set<Class<? extends DatabaseTestBuilder>> databaseTestBuilders = reflactions.getSubTypesOf(DatabaseTestBuilder.class);

        for (Class<? extends DatabaseTestBuilder> databaseTestBuilder : databaseTestBuilders) {
            if (!databaseTestBuilder.isAnnotationPresent(Repository.class)) {
                fail(MessageFormat.format(NO_REPOSITORY_ANNOTATION_MESSAGE, databaseTestBuilder.getSimpleName()));
            }
        }
    }
}
