package com.infy.infyinterns;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.service.ProjectAllocationService;
import com.infy.infyinterns.service.ProjectAllocationServiceImpl;

@SpringBootTest
public class InfyInternsApplicationTests {

	@Mock
	private MentorRepository mentorRepository;

	@InjectMocks
	private ProjectAllocationService projectAllocationService = new ProjectAllocationServiceImpl();

	@Test
	void allocateProjectCannotAllocateTest() throws Exception {
		MentorDTO mentorDTO=new MentorDTO();
		mentorDTO.setMentorId(4);
		ProjectDTO projDTO=new ProjectDTO();
		projDTO.setMentorDTO(mentorDTO);
		Mentor mentorEntity=new Mentor();
		mentorEntity.setMentorId(4);
		mentorEntity.setNumberOfProjectsMentored(5);
		Mockito.when(mentorRepository.findById(projDTO.getMentorDTO().getMentorId())).thenReturn(Optional.ofNullable(mentorEntity));
		InfyInternException exObj=assertThrows(InfyInternException.class, ()->projectAllocationService.allocateProject(projDTO));
		String expected="Service.CANNOT_ALLOCATE_PROJECT";
		assertEquals(expected, exObj.getMessage());
	}

	@Test
	void allocateProjectMentorNotFoundTest() throws Exception {
	

	}
}