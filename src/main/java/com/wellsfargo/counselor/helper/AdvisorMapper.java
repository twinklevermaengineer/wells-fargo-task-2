package com.wellsfargo.counselor.helper;

import org.springframework.stereotype.Component;

import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.model.response.AdvisorResponse;

/**
 * A helper component that maps {@link Advisor} entities to {@link AdvisorResponse} DTOs.
 * <p>
 * This class is annotated with {@link Component}, making it a spring managed bean,
 * which can be injected whereever entity-to-DTO mapping is required.
 * </p>
 * It provides a utility method to convert {@link Advisor} objects into
 * {@link AdvisorResponse} objects, typically used in service or controller layers
 * to return DTOs instead of entities.
 * </p>
*/
@Component
public class AdvisorMapper {
    /**
     * Default constructor.
     * <p>
     * Spring requires a default constructor to manage this component as a bean.
     * </p>
     */
	
	public AdvisorMapper() {
		
	}
	
	/**
     * Converts a {@link Advisor} entity into an {@link AdvisorResponse} DTO.
     * <p>
     * This method safely handles null input and returns null if the provided
     * {@link Advisor} is null.
     * </p>
     *
     * @param advisor the {@link Advisor} entity to convert; may be null
     * @return an {@link AdvisorResponse} DTO containing the same information
     *         as the entity, or null if the input is null
     */
	
	public static AdvisorResponse mapEntityToResponse(Advisor advisor) {
		if(advisor == null) {
			return null;
		}
		return new AdvisorResponse(
				advisor.getAdvisorId(),
				advisor.getFirstName(),
				advisor.getLastName(),
				advisor.getAddress(),
				advisor.getPhone(),
				advisor.getEmail()
		);	
	}
}
