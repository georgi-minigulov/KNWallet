package org.gfm.wallet.repository;

import java.util.List;

import org.gfm.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
	@Query(value = "SELECT * FROM testschema.t_wallets WHERE user_id = ?1", nativeQuery = true)
	List<Wallet> findByUser(Long userId);
	@Query(value = "SELECT * FROM testschema.t_wallets WHERE user_id = ?1 and currency_code = ?2", nativeQuery = true)
	Wallet findByUserAndCurrency(Long userId, String currencyCode);
}
