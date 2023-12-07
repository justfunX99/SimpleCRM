package com.justfun.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkClientItemVO {
	
	private BulkClientItemCellVO bulkClientItemCellVO;
	
	private Integer result; // 0: success; 1: fail
	
	private String msg;
	
	
}
