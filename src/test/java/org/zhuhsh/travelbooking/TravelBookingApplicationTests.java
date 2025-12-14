package org.zhuhsh.travelbooking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TravelBookingApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnToursList() throws Exception {
        mockMvc.perform(get("/api/tours"))
                .andExpect(status().isOk());
    }

    @Test
    void loginShouldFailWithWrongCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("""
                {
                  "username": "wrong",
                  "password": "wrong"
                }
            """))
                .andExpect(status().isUnauthorized());
    }

}
