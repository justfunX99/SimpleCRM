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

import com.justfun.dto.CompanyDTO;
import com.justfun.entity.Company;
import com.justfun.service.CompanyService;

// TODO: define response in detail
@RestController
@RequestMapping("api/companies")
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	@PreAuthorize("hasAuthority(@A.CREATE_COMPANY)")
	@PostMapping("add")
	public ResponseEntity<Company> createCompany(@RequestBody CompanyDTO dto) {
		Company res = companyService.add(dto);
		return ResponseEntity.ok(res);
	}
	
	@PreAuthorize("hasAuthority(@A.MODIFY_COMPANY)")
	@PutMapping("{id}")
	public ResponseEntity<Company> modifyCompany(@PathVariable Integer id, @RequestBody CompanyDTO dto
	) {
		Company res = companyService.update(id, dto);
		return ResponseEntity.ok(res);
	}
	
	@PreAuthorize("hasAuthority(@A.DELETE_COMPANY)")
	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteCompany(@PathVariable Integer id) {
		companyService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAuthority(@A.VIEW_COMPANY)")
	@GetMapping("{id}")
	public ResponseEntity<Company> find(@PathVariable Integer id) {
		Company res = companyService.findById(id);
		return ResponseEntity.ok(res);
	}
	
	@PreAuthorize("hasAuthority(@A.VIEW_COMPANY)")
	@GetMapping
	public ResponseEntity<List<Company>> findAll() {
		List<Company> res = companyService.findAll();
		return ResponseEntity.ok(res);
	}
}
