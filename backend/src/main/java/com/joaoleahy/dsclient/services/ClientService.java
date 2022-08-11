package com.joaoleahy.dsclient.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joaoleahy.dsclient.entities.Client;
import com.joaoleahy.dsclient.repositories.ClientRepository;
import com.joaoleahy.dsclient.services.exceptions.ResourceNotFound;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public Page<Client> findAllPagination(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return list;
	}

	@Transactional(readOnly = true)
	public Client findById(Long id) {
		Optional<Client> client = repository.findById(id);
		return client.orElseThrow(() -> new ResourceNotFound("Resource not founded!"));
	}

	@Transactional
	public Client insert(Client client) {
		return repository.save(client);
	}

	@Transactional
	public Client update(Long id, Client client) {
		try {
			Client clientUpdate = repository.getReferenceById(id);
			updateClient(clientUpdate, client);
			return repository.save(clientUpdate);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFound("Resource not founded");
		}
	}

	@Transactional
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFound("Resource not founded");
		}
	}

	private void updateClient(Client clientUpdate, Client client) {
		clientUpdate.setName(client.getName());
		clientUpdate.setCpf(client.getCpf());
		clientUpdate.setIncome(client.getIncome());
		clientUpdate.setChildren(client.getChildren());
		clientUpdate.setBirthDate(client.getBirthDate());

	}
}