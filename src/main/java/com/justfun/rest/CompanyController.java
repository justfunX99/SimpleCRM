package com.justfun.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.justfun.dto.CompanyDto;

// TODO: CRM logic(CRUD)
@RestController
@RequestMapping("api/companies")
public class CompanyController {

	@PreAuthorize("hasAuthority(@A.CREATE_COMPANY)")
	@PostMapping("add")
	public String createCompany(@RequestBody CompanyDto dto) {
		System.out.println("### createCompany. dto: " + dto.toString());
		return "ok";
	}
	
	@PreAuthorize("hasAuthority(@A.MODIFY_COMPANY)")
	@PutMapping("{id}")
	public String modifyCompany(@PathVariable Integer id, @RequestBody CompanyDto dto
	) {
		System.out.println("### modifyCompany. id: " + id + ", dto: " + dto.toString());
		return "ok";
	}
	
	@PreAuthorize("hasAuthority(@A.DELETE_COMPANY)")
	@DeleteMapping("{id}")
	public String deleteCompany(@PathVariable Integer id) {
		System.out.println("### deleteCompany. id: " + id);
		return "ok";
	}
	
	@PreAuthorize("hasAuthority(@A.VIEW_COMPANY)")
	@GetMapping("{id}")
	public String find(@PathVariable Integer id) {
		System.out.println("### find. id: " + id);
		return "ok";
	}
	
	@PreAuthorize("hasAuthority(@A.VIEW_COMPANY)")
	@GetMapping
	public String findAll() {
		System.out.println("### findAll");
		return "ok";
	}
}
