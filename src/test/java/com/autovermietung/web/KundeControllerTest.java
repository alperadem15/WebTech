package com.autovermietung.web;

import com.autovermietung.user.Kunde;
import com.autovermietung.user.KundeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KundeController.class)
class KundeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KundeRepository kundeRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void register_savesCustomer_andHashesPassword() throws Exception {
        // given
        when(kundeRepository.findByEmail("a@b.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Test123!")).thenReturn("HASH");
        when(kundeRepository.save(any(Kunde.class))).thenAnswer(inv -> inv.getArgument(0));

        // when / then
        mockMvc.perform(post("/kunde/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "a@b.com",
                                  "password": "Test123!",
                                  "vorname": "Max",
                                  "nachname": "Mustermann"
                                }
                                """))
                .andExpect(status().isOk());

        // capture saved customer and assert password hashed
        ArgumentCaptor<Kunde> captor = ArgumentCaptor.forClass(Kunde.class);
        verify(kundeRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo("HASH");
    }

    @Test
    void login_success_returns200() throws Exception {
        // given: stored user has HASH, and matches() returns true
        Kunde stored = new Kunde("a@b.com", "HASH", "Max", "Mustermann");
        stored.setId(1L);

        when(kundeRepository.findByEmail("a@b.com")).thenReturn(Optional.of(stored));
        when(passwordEncoder.matches("Test123!", "HASH")).thenReturn(true);

        // when / then
        mockMvc.perform(post("/kunde/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "a@b.com",
                                  "password": "Test123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Login erfolgreich")));
    }
}
