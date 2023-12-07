package com.justfun.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class BulkClientDTO {
	private int total;
	
	private List<ClientDTO> clients;

}
