package com.justfun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientDTO {
	
	@NonNull
	private String name;
	
	@NonNull
	private String email;
	
	@NonNull
	private String phone;
	
	private Integer belongCompanyId;
}
