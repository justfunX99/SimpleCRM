package com.justfun.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class ClientDTO {
	
	@NonNull
	private String name;
	
	private String email;
	
	private String phone;
	
	private Integer belongCompanyId;
}
