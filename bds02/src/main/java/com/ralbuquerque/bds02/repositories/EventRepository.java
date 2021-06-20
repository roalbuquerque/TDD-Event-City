package com.ralbuquerque.bds02.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ralbuquerque.bds02.entities.Event;

public interface EventRepository extends JpaRepository<Event, Long>{

}
