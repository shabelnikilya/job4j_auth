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
import ru.job4j.domain.Employee;
import ru.job4j.domain.Person;
import ru.job4j.service.EmployeeService;
import ru.job4j.service.PersonService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private PersonService personService;
    @MockBean
    private EmployeeService employeeService;

    @Test
    public void testPersonControllerFindAll() throws Exception {
        Iterable<Person> exp = List.of(new Person("login", "password"),
                new Person("second login", "second password"));
        Mockito.when(personService.findAll()).thenReturn(exp);
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
        Mockito.when(personService.findById(1)).thenReturn(Optional.of(person));
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/person/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.login", is("login")));
    }

    @Test
    public void testPersonControllerFindByIdWhenNotFindPerson() throws Exception {
        Mockito.when(personService.findById(1)).thenReturn(Optional.empty());
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
        Mockito.when(personService.save(person)).thenReturn(person);
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

    @Test
    public void testEmployeeControllerFindAll() throws Exception {
        Set<Person> persons = Set.of(new Person(1, "login", "password"),
                new Person(2, "second login", "second password"));
        Employee employee = new Employee("name", "surname", "11-22-33", new Date(), persons);
        Iterable<Employee> exp = List.of(employee);
        Mockito.when(employeeService.findAll()).thenReturn(exp);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("name")))
                .andExpect(jsonPath("$[0].surname", is("surname")))
                .andExpect(jsonPath("$[0].accounts", hasSize(2)));
    }

    @Test
    public void testEmployeeControllerFindByIdWhenFindEmployee() throws Exception {
        Person person = new Person(1, "login", "password");
        Employee employee = new Employee("name", "surname", "11-22-33", new Date(), Set.of(person));
        Mockito.when(employeeService.findById(1)).thenReturn(Optional.of(employee));
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.surname", is("surname")));
    }

    @Test
    public void testEmployeeControllerFindByIdWhenNotFindEmployee() throws Exception {
        Mockito.when(employeeService.findById(1)).thenReturn(Optional.empty());
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/employee/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testEmployeeControllerWhenCreateOneEmployee() throws Exception {
        Person person = new Person("login", "password");
        Employee employee = new Employee("name", "surname", "11-22-33", new Date(), Set.of(person));
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/employee/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(person));
        Mockito.when(employeeService.save(employee)).thenReturn(employee);
        this.mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.surname", is("surname")));
    }

    @Test
    public void testEmployeeControllerWhenUpdateEmployee() throws Exception {
        Person person = new Person("login", "password");
        Employee employee = new Employee("name", "surname", "11-22-33", new Date(), Set.of(person));
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/employee/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(employee));
        this.mockMvc.perform(mockRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void testEmployeeControllerWhenDeleteEmployee() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/employee/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
