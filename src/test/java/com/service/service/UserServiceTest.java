package com.service.service;

import com.service.app.entity.User;
import com.service.app.repository.UserRepository;
import com.service.app.service.UserService;
import com.service.app.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void testGetUserByUsername() {
        User user = new User();
        user.setUsername("JonkiPro");

        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findByUsernameIgnoreCaseAndEnabledTrue("JonkiPro")).thenReturn(userOptional);

        String username = userService.findByUsername("JonkiPro").get().getUsername();

        verify(userRepository, times(1)).findByUsernameIgnoreCaseAndEnabledTrue("JonkiPro");

        assertEquals("JonkiPro", username);
    }

    @Test
    public void testUserNotPresent(){
        when(userRepository.findByUsernameIgnoreCaseAndEnabledTrue("qwerty")).thenReturn(null);

        Optional<User> userOptional = userService.findByUsername("qwerty");

        assertEquals(null, userOptional);
    }
}
