package com.ralbuquerque.bds02.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ralbuquerque.bds02.dto.EventDTO;
import com.ralbuquerque.bds02.entities.City;
import com.ralbuquerque.bds02.entities.Event;
import com.ralbuquerque.bds02.repositories.CityRepository;
import com.ralbuquerque.bds02.repositories.EventRepository;
import com.ralbuquerque.bds02.services.exceptions.DatabaseException;
import com.ralbuquerque.bds02.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository repository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Transactional(readOnly = true)
	public Page<EventDTO> findAll(Pageable pageable) {
		Page <Event> list = repository.findAll(pageable);
		return list.map(x -> new EventDTO(x));
	}
	
	@Transactional
	public EventDTO insert(EventDTO dto) {
		Event entity = new Event();
		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setUrl(dto.getUrl());
		entity.setCity(new City(dto.getCityId(), null));
		entity = repository.save(entity);
		return new EventDTO(entity);
	}
	
	@Transactional
	public EventDTO update(Long id, EventDTO dto) {
		try {
			Event entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			
			entity = repository.save(entity);
			return new EventDTO(entity);
		}
		catch(EntityNotFoundException e){
			throw new ResourceNotFoundException("Id " + id + " not found");
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id " + id + " not found");
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	private void copyDtoToEntity(EventDTO dto, Event entity) {
		
		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setUrl(dto.getUrl());
		City city = cityRepository.getOne(dto.getCityId());
		entity.setCity(city);
	}
	
}
