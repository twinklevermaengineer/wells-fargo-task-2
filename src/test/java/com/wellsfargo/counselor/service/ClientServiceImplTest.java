package com.wellsfargo.counselor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.model.request.ClientRequest;
import com.wellsfargo.counselor.model.request.ClientResponse;
import com.wellsfargo.counselor.repository.ClientRepository;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.utils.ClientRequestValidator;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

	@Mock
	private ClientRepository clientRepository;
	
	@Mock
	private ClientRequestValidator validator;

	@InjectMocks
	private ClientServiceImpl clientServiceImpl;
	
	private ClientRequest clientRequest;
	
	private Client client;
	
	@BeforeEach
	void setUp() {
		clientRequest = new ClientRequest();
		clientRequest.setFirstName("John");
		clientRequest.setLastName("Thompson");		
		clientRequest.setAddress("879, Miami Fl");
		clientRequest.setPhone("9985647123");
		clientRequest.setEmail("johnthompson@gmail.com");
	}
	
	@Test
	void findById_whenClientFoundWithId_success() {
		//Arrange
		client = new Client();
		Long id = 6L;
		when(clientRepository.findById(id)).thenReturn(Optional.of(client));
		
		//Act
		ClientResponse result = clientServiceImpl.findById(id);
		
		//Assert
		assertNotNull(result);
		verify(clientRepository).findById(id);
	}
	
	@Test
	void findById_whenNoClientFoundWithId_fail() {
		//Arrange
		Long id = 1L;
		when(clientRepository.findById(id)).thenReturn(Optional.empty());

		//Act
		ClientResponse response = clientServiceImpl.findById(id);
		
		//Assert

		assertNull(response);
		verify(clientRepository).findById(id);

	}
	
	@Test
	void findByAddress_whenClientFoundWithAddress_success() {
		//Arrange
		client = new Client();
		client.setAddress(clientRequest.getAddress());
		String address = client.getAddress();
		when(clientRepository.findByAddress(address)).thenReturn(List.of(client));
		
		//Act
		List<ClientResponse> result = clientServiceImpl.findByAddress(address);
		
		//Assert
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(clientRepository).findByAddress(address);
	}
	
	@Test
	void findByAddress_whenNoClientsFound_returnEmptyList() {
		//Arrange
		
		String address = clientRequest.getAddress();
		when(clientRepository.findByAddress(address)).thenReturn(List.of());
		
		//Act
		List<ClientResponse> result = clientServiceImpl.findByAddress(address);
		
		//Assert
		assertTrue(result.isEmpty());
		verify(clientRepository).findByAddress(address);
		
	}

	@Test
	void findAll_whenClientFound_returnClientList_success() {
		//Arrange
		client = new Client("John", "Richard", "johnrichard@gmail.com", "9594565214", "568, Tampa Fl");
		List<Client> clientList = new ArrayList<>();
		clientList.add(client);
		when(clientRepository.findAll()).thenReturn(List.of(client));
		
		//Act
		List<ClientResponse> result = clientServiceImpl.findAll();
		
		//Assert
		assertNotNull(result);
		assertTrue(!result.isEmpty());
		assertEquals(1, result.size());
		ClientResponse response = result.get(0);
		assertEquals(client.getClientId(), response.getClientId());
		assertEquals(client.getFirstName(), response.getFirstName());
		assertEquals(client.getLastName(), response.getLastName());
		assertEquals(client.getAddress(), response.getAddress());
		assertEquals(client.getPhone(), response.getPhone());
		assertEquals(client.getEmail(), response.getEmail());
		verify(clientRepository,times(1)).findAll();
	}
	
	@Test
	void findAll_whenNoClientsFound_returnEmptyList() {
		//Arrange
		client = new Client();
		when(clientRepository.findAll()).thenReturn(List.of());
		
		//Act
		List<ClientResponse> result = clientServiceImpl.findAll();
		
		//Assert
		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(clientRepository).findAll();
	}
	
	@Test
	void save_whenClientSaved_success() {
		//Arrange
		client = new Client(
			clientRequest.getFirstName(), clientRequest.getLastName(),
			clientRequest.getEmail(), clientRequest.getPhone(),
			clientRequest.getAddress());
		when(clientRepository.save(any(Client.class))).thenReturn(client);
		
		//Act
		ClientResponse clientResponse = clientServiceImpl.save(clientRequest);
		
		//Assert
		assertNotNull(clientResponse);
		assertEquals(clientRequest.getFirstName(), clientResponse.getFirstName());
		verify(validator, times(1)).validateClientRequest(eq(clientRequest), isNull(), anyList(), eq("create"));
		verify(clientRepository, times(1)).save(any(Client.class));
		
	}
	
	@Test
	void save_whenNoClientSaved_fail() {
		
		//Arrange
		when(clientRepository.save(any(Client.class))).thenReturn(null);

		//Act
		ClientResponse response = clientServiceImpl.save(clientRequest);
			
		//Assert
		assertNull(response);
		
		verify(validator, times(1))
				.validateClientRequest(eq(clientRequest),isNull(), anyList(), eq("create"));
		
		verify(clientRepository, times(1))
				.save(any(Client.class));
		
	}
	
	@Test
	void update_whenUpdated_success() {
		//Arrange
		Long id = 1L;
		client = new Client(
			clientRequest.getFirstName(), clientRequest.getLastName(),
			clientRequest.getEmail(),
			clientRequest.getPhone(),
			clientRequest.getAddress());

		when(clientRepository.findById(id)).thenReturn(Optional.of(client));
		when(clientRepository.save(any(Client.class))).thenReturn(client);
		
		//Act
		Client result = clientServiceImpl.updateClient(id, clientRequest);
		
		//Assert
		
		assertNotNull(result);
		assertEquals(client.getFirstName(), result.getFirstName());
		assertEquals(client.getLastName(), result.getLastName());
		assertEquals(client.getAddress(), result.getAddress());
		assertEquals(client.getPhone(), result.getPhone());
		assertEquals(client.getEmail(), result.getEmail());
		verify(clientRepository, times(1)).findById(id);
		verify(clientRepository, times(1)).save(any(Client.class));

	}
	
	@Test
	void updateClient_whenNotUpdated_fail() {
		//Arrange
		Long id = 1L;

		when(clientRepository.findById(id)).thenReturn(Optional.empty());
		
		//Act and Assert
		ResourceNotFoundException exception = assertThrows(
				ResourceNotFoundException.class,
				() -> clientServiceImpl.updateClient(id, clientRequest));
		

		assertTrue(exception.getMessage().contains("Client not found with id " + id));
		
		verify(clientRepository, times(1)).findById(id);
		verify(clientRepository, never()).save(any());
	}	
}
