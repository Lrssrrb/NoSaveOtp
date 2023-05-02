package com.service;

import com.entity.EntityTable;

public interface Services {

	public String get(EntityTable otpEntity);
	public boolean varify(EntityTable otpEntity);
	
}
