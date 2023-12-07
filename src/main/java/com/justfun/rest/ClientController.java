package com.justfun.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.justfun.dto.BulkClientDTO;
import com.justfun.dto.ClientDTO;
import com.justfun.entity.Client;
import com.justfun.service.ClientService;
import com.justfun.vo.BulkClientVO;

//TODO: define response in detail
@RestController
@RequestMapping("api/clients")
public class ClientController {

	@Autowired
	private ClientService clientService;
	
	@PreAuthorize("hasAuthority(@A.CREATE_CLIENT)")
	@PostMapping("add")
	public ResponseEntity<Client> addClient(@RequestBody ClientDTO dto) {
		Client res = clientService.add(dto);
		return ResponseEntity.ok(res);
	}
	
	@PreAuthorize("hasAuthority(@A.CREATE_CLIENT)")
	@PostMapping("bulkAdd")
	public ResponseEntity<BulkClientVO> bulkAddClient(@RequestBody BulkClientDTO dto) {
		BulkClientVO res = clientService.add(dto);
		return ResponseEntity.ok(res);
	}
	
	@PreAuthorize("hasAuthority(@A.MODIFY_CLIENT)")
	@PutMapping("{id}")
	public ResponseEntity<Client> updateClient(@PathVariable Integer id, @RequestBody ClientDTO dto
	) {
		Client res = clientService.update(id, dto);
		return ResponseEntity.ok(res);
	}
	
	@PreAuthorize("hasAuthority(@A.DELETE_CLIENT)")
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
		clientService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAuthority(@A.VIEW_CLIENT)")
	@GetMapping("{id}")
	public ResponseEntity<Client> find(@PathVariable Integer id) {
		Client res = clientService.findById(id);
		return ResponseEntity.ok(res);
	}
	
	@PreAuthorize("hasAuthority(@A.VIEW_CLIENT)")
	@GetMapping
	public ResponseEntity<List<Client>> findAll() {
		List<Client> res = clientService.findAll();
		return ResponseEntity.ok(res);
	}
	
}
