package org.gfm.wallet.requests;

public class TransferRequest {
	private Long sourceUserId;
	private Long sourceWalletId;
	private Long targetUserId;
	private Long targetWalletId;
	private String sum;
	
	public Long getSourceUserId() {
		return sourceUserId;
	}
	public Long getSourceWalletId() {
		return sourceWalletId;
	}
	public Long getTargetUserId() {
		return targetUserId;
	}
	public Long getTargetWalletId() {
		return targetWalletId;
	}
	public String getSum() {
		return sum;
	}

}
