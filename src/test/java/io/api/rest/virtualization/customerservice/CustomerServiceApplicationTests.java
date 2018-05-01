package io.api.rest.virtualization.customerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.api.rest.virtualization.customerservice.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerServiceApplicationTests {

	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void getCustomers() throws Exception {
		mockMvc.perform(get("/customers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.*", hasSize(5)));
	}

	@Test
	public void getCustomer() throws Exception {
		mockMvc.perform(get("/customers/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("1")))
				.andExpect(jsonPath("$.salutation", is("Mr.")))
				.andExpect(jsonPath("$.firstName", is("FirstName1")))
				.andExpect(jsonPath("$.lastName", is("LastName1")))
				.andExpect(jsonPath("$.dateOfBirth", is(simpleDateFormat.format(new Date()))));
	}

    @Test
    public void getNonExistingCustomer() throws Exception {
        mockMvc.perform(get("/customers/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        Customer customer = Customer.builder().id("10").salutation("Mr.").firstName("FirstName10").lastName("LastName10").dateOfBirth(new Date()).build();
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated());
        // Verify created customer
        mockMvc.perform(get("/customers/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("10")))
                .andExpect(jsonPath("$.salutation", is("Mr.")))
                .andExpect(jsonPath("$.firstName", is("FirstName10")))
                .andExpect(jsonPath("$.lastName", is("LastName10")))
                .andExpect(jsonPath("$.dateOfBirth", is(simpleDateFormat.format(new Date()))));
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        Customer customer = Customer.builder().id("10").salutation("Mr.").firstName("FirstName10").lastName("LastName10").dateOfBirth(new Date()).build();
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated());
        // Update customer
        mockMvc.perform(put("/customers/10")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"dateOfBirth\":\"31-01-2012\"}"))
                .andExpect(status().isOk());
        // Verify created customer
        mockMvc.perform(get("/customers/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("10")))
                .andExpect(jsonPath("$.salutation", is("Mr.")))
                .andExpect(jsonPath("$.firstName", is("FirstName10")))
                .andExpect(jsonPath("$.lastName", is("LastName10")))
                .andExpect(jsonPath("$.dateOfBirth", is("31-01-2012")));
    }

    @Test
    @Transactional
    public void updateNonExistingCustomer() throws Exception {
        mockMvc.perform(put("/customers/10")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"dateOfBirth\":\"31-01-2012\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        Customer customer = Customer.builder().id("10").salutation("Mr.").firstName("FirstName10").lastName("LastName10").dateOfBirth(new Date()).build();
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated());
        // Delete customer
        mockMvc.perform(delete("/customers/10"))
                .andExpect(status().isNoContent());
        // Verify delete is successful
        mockMvc.perform(get("/customers/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteNonExistingCustomer() throws Exception {
        mockMvc.perform(delete("/customers/10"))
                .andExpect(status().isNotFound());
    }
}
