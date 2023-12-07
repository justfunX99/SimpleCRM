package com.justfun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkClientItemCellVO {
	private Integer id;
	
	private String name;
	
	private String email;
	
	private String phone;
	
	private CompanyVO company;
}
