package com.task.task.repository;

import com.task.task.model.Proyecto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProyectoRepository extends MongoRepository<Proyecto, String> {
}
