package com.ralbuquerque.bds02.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ralbuquerque.bds02.dto.EventDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EventControllerIT {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void findAllShouldReturnPagedResourcesSortedByName() throws Exception {
		
		ResultActions result =
				mockMvc.perform(get("/events")
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("CCXP"));
		result.andExpect(jsonPath("$.content[1].name").value("Congresso Linux"));
		result.andExpect(jsonPath("$.content[2].name").value("Feira do Software"));
		result.andExpect(jsonPath("$.content[3].name").value("Semana Spring React"));
		
	}
	
	@Test
	public void insertShouldInsertResource() throws Exception {

		EventDTO dto = new EventDTO(null, "Feira do DevOps", LocalDate.of(2021, 6, 16), "https://feiradosoftware.com", 1L);
		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result =
				mockMvc.perform(post("/events")
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").value("Feira do DevOps"));
		result.andExpect(jsonPath("$.date").value("2021-06-16"));
		result.andExpect(jsonPath("$.url").value("https://feiradosoftware.com"));
		result.andExpect(jsonPath("$.cityId").value(1L));
	}	
	
	@Test
	public void updateShouldUpdateResourceWhenIdExists() throws Exception {

		long existingId = 1L;
		
		EventDTO dto = new EventDTO(null, "Expo XP", LocalDate.of(2021, 5, 18), "https://expoxp.com.br", 7L);
		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result =
				mockMvc.perform(put("/events/{id}", existingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.id").value(1L));		
		result.andExpect(jsonPath("$.name").value("Expo XP"));
		result.andExpect(jsonPath("$.date").value("2021-05-18"));
		result.andExpect(jsonPath("$.url").value("https://expoxp.com.br"));
		result.andExpect(jsonPath("$.cityId").value(7L));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

		long nonExistingId = 1000L;
		
		EventDTO dto = new EventDTO(null, "Expo XP", LocalDate.of(2021, 5, 18), "https://expoxp.com.br", 7L);
		String jsonBody = objectMapper.writeValueAsString(dto);
		
		ResultActions result =
				mockMvc.perform(put("/events/{id}", nonExistingId)
					.content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenNonExistingId() throws Exception {		

		Long nonExistingId = 50L;
		
		ResultActions result =
				mockMvc.perform(delete("/events/{id}", nonExistingId));

		result.andExpect(status().isNotFound());
	}
}
