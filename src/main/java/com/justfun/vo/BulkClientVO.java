package com.justfun.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkClientVO {
	
	private int total;
	
	private int numOfSuccess;
	
	private int numOfFail;
	
	private List<BulkClientItemVO> bulkClientItemVOs;

}
