package com.hunt.worker-service-root;

import com.hunt.worker-service-root.business.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class TestObjectJSONReaderTest {

    @InjectMocks
    private TestObjectJSONReader testObjectJSONReader;

    @Test
    public void readUsers() {
        //WHEN
        List<User> tags = TestObjectJSONReader.read(User.class, "users.json");

        //THEN
        assertNotNull(tags);
        assertEquals(1, tags.size());
    }
}