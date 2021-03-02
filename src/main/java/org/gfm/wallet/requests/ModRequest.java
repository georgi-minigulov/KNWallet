package org.gfm.wallet.requests;

public class ModRequest {
	private Long userId;
	private Long walletId;
	private String sum;
	
	public Long getUserId() {
		return userId;
	}
	public Long getWalletId() {
		return walletId;
	}
	public String getSum() {
		return sum;
	}
}
