package org.gfm.wallet.repository;

import java.util.List;

import org.gfm.wallet.model.HistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HistoryRepository extends JpaRepository<HistoryRecord, Long> {
	@Query(value = "SELECT * FROM testschema.t_history WHERE (source_name = ?1 and source_id = ?2) or (target_name = ?1 and target_id = ?2)", nativeQuery = true)
	List<HistoryRecord> findForObject(String name, Long id);
}
