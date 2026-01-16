package com.autovermietung.web;

import com.autovermietung.CarRepository;
import com.autovermietung.UmsatzRepository;
import com.autovermietung.user.Autovermieter;
import com.autovermietung.user.AutovermieterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AutovermieterController.class)
class AutovermieterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutovermieterRepository vermieterRepository;

    @MockBean
    private CarRepository carRepository;

    @MockBean
    private UmsatzRepository umsatzRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    // ✅ TEST 1: Login erfolgreich
    @Test
    void login_success_returns200() throws Exception {
        Autovermieter stored = new Autovermieter();
        stored.setId(1L);
        stored.setEmail("v@b.com");
        stored.setPassword("HASH");

        when(vermieterRepository.findByEmail("v@b.com"))
                .thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("Test123!", "HASH"))
                .thenReturn(true);

        mockMvc.perform(post("/vermieter/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "v@b.com",
                                  "password": "Test123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString("Login erfolgreich")));
    }

    // ❌ TEST 2: Falsches Passwort
    @Test
    void login_wrongPassword_throwsRuntimeException() {
        Autovermieter stored = new Autovermieter();
        stored.setId(1L);
        stored.setEmail("v@b.com");
        stored.setPassword("HASH");

        when(vermieterRepository.findByEmail("v@b.com"))
                .thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("WRONG", "HASH"))
                .thenReturn(false);

        assertThatThrownBy(() ->
                mockMvc.perform(post("/vermieter/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "v@b.com",
                                          "password": "WRONG"
                                        }
                                        """))
                        .andReturn()
        )
                .hasRootCauseInstanceOf(RuntimeException.class)
                .rootCause()
                .hasMessageContaining("Email oder Passwort falsch");
    }

    // ❌ TEST 3: Email existiert nicht
    @Test
    void login_unknownEmail_throwsRuntimeException() {
        when(vermieterRepository.findByEmail("missing@b.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                mockMvc.perform(post("/vermieter/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "missing@b.com",
                                          "password": "whatever"
                                        }
                                        """))
                        .andReturn()
        )
                .hasRootCauseInstanceOf(RuntimeException.class)
                .rootCause()
                .hasMessageContaining("Email oder Passwort falsch");
    }
}
