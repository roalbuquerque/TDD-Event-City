package com.ralbuquerque.bds02.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ralbuquerque.bds02.dto.CityDTO;
import com.ralbuquerque.bds02.entities.City;
import com.ralbuquerque.bds02.repositories.CityRepository;

@Service
public class CityService {

	@Autowired
	private CityRepository repository;
	
	public List<CityDTO> findAll(){
		List<City> list = repository.findAll(Sort.by("name"));
		return list.stream().map(x -> new CityDTO(x)).collect(Collectors.toList());
	}
}
