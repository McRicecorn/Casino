package com.example.casino.casino.Repository;

import com.example.casino.casino.Model.SlotmachineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISlotmachineRepository extends JpaRepository<SlotmachineEntity, Long> {

}
