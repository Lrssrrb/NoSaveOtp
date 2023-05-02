package com.entity;

import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EntityTable {

	private String receiver;
	@Transient
	private String otp;
	
}
