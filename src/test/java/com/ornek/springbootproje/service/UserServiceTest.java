package com.ornek.springbootproje.service;

import com.ornek.springbootproje.entities.*;
import com.ornek.springbootproje.repository.*;
import com.ornek.springbootproje.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private AccountVerificationRepository accountVerificationRepository;

    @Mock
    private ResetPasswordVerificationRepository resetPasswordVerificationRepository;

    @Mock
    private MailService mailService;

    @Mock
    private JwtTokenUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser_WhenEmailExists_ThrowsException() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(Exception.class, () -> {
            userService.addUser(user);
        });

        String expectedMessage = "Bu e-posta adresi zaten kayıtlı.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testAddUser_Success() throws Exception {
        User user = new User();
        user.setEmail("newuser@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(user);
        verify(mailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void testGetUserById_Success() {
        // Arrange: Create a mock user object
        User user = new User();
        user.setId(1L);

        // Mocking the repository behavior to return the user when userRepository.findById is called
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act: Call the service method
        User foundUser = userService.getUserById(1L);

        // Assert: Verify the result
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
    }


    @Test
    public void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User user = userService.getUserById(1L);

        assertNull(user);
    }

    @Test
    public void testDeleteUser_Success() {
        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testVerifyAccount_Success() throws Exception {
        AccountVerification accountVerification = new AccountVerification();
        accountVerification.setUserId(1L);

        User user = new User();
        user.setId(1L);
        user.setIsactive(false);

        when(accountVerificationRepository.findByUrl(anyString())).thenReturn(Optional.of(accountVerification));

        when(userRepository.findById(accountVerification.getUserId())).thenReturn(Optional.of(user));

        boolean result = userService.verifyAccount("verificationUrl");

        assertTrue(result);
        assertTrue(user.getIsactive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testVerifyAccount_Fail() throws Exception {
        when(accountVerificationRepository.findByUrl(anyString())).thenReturn(Optional.empty());

        boolean result = userService.verifyAccount("invalidUrl");

        assertFalse(result);
    }
}