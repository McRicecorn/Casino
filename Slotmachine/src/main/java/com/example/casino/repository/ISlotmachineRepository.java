package com.example.casino.repository;

import com.example.casino.model.ISlotmachineGameEntity;
import com.example.casino.model.SlotmachineGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISlotmachineRepository extends JpaRepository<SlotmachineGameEntity, Long> {
    List<ISlotmachineGameEntity> findByUserId(long userId);
}
