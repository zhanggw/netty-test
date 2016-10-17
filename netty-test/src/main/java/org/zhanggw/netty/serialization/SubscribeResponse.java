package org.zhanggw.netty.serialization;

import java.io.Serializable;

public class SubscribeResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6577347938572398939L;
	private int subReqId;
	private int respCode;
	private String desc;
	public int getSubReqId() {
		return subReqId;
	}
	public void setSubReqId(int subReqId) {
		this.subReqId = subReqId;
	}
	public int getRespCode() {
		return respCode;
	}
	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
	public String toString() {
		return "Request [subReqId=" + subReqId + ", respCode=" + respCode + ", desc=" + desc + "]";
	}
}
