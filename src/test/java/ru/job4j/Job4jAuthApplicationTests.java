package ru.job4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.job4j.domain.Person;
import ru.job4j.service.PersonService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Job4jAuthApplication.class)
@AutoConfigureMockMvc
class Job4jAuthApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private PersonService service;

    @Test
    public void testPersonControllerFindAll() throws Exception {
        Iterable<Person> exp = List.of(new Person("login", "password"),
                new Person("second login", "second password"));
        Mockito.when(service.findAll()).thenReturn(exp);
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/person/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].login", is("login")))
                .andExpect(jsonPath("$[1].login", is("second login")));
    }

    @Test
    public void testPersonControllerFindByIdWhenFindPerson() throws Exception {
        Person person = new Person(1, "login", "password");
        Mockito.when(service.findById(1)).thenReturn(Optional.of(person));
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/person/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.login", is("login")));
    }

    @Test
    public void testPersonControllerFindByIdWhenNotFindPerson() throws Exception {
        Mockito.when(service.findById(1)).thenReturn(Optional.empty());
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/person/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPersonControllerWhenCreateOnePerson() throws Exception {
        Person person = new Person("login", "password");
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(person));
        Mockito.when(service.save(person)).thenReturn(person);
        this.mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.login", is("login")));
    }

    @Test
    public void testPersonControllerWhenUpdatePerson() throws Exception {
        Person person = new Person("login", "password");
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(person));
        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void testPersonControllerWhenDeletePerson() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/person/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
