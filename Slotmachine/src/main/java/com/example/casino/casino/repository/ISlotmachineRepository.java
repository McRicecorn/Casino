package com.example.casino.casino.repository;

import com.example.casino.casino.model.SlotmachineGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISlotmachineRepository extends JpaRepository<SlotmachineGameEntity, Long> {

}
