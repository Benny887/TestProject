package com.example.sweater.repos;

import com.example.sweater.domain.Animals;
import com.example.sweater.domain.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface AnimalsRepo extends CrudRepository<Animals, Long> {
    List<Animals> findByMaster(User master);

    Animals findById(int id);
}
