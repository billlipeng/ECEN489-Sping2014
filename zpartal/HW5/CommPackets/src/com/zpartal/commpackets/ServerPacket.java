package com.zpartal.commpackets;

import java.io.Serializable;

public class ServerPacket implements Serializable {
	private String serverID;
	private int result;
	
	public ServerPacket(String serverID, int result) {
		super();
		this.serverID = serverID;
		this.result = result;
	}
	public String getServerID() {
		return serverID;
	}
	public void setServerID(String serverID) {
		this.serverID = serverID;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
}
