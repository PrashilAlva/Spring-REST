package com.infy.infyinterns.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.service.ProjectAllocationService;

@RestController
@Validated
@RequestMapping(value="/infyinterns")
public class ProjectAllocationAPI
{
	
	@Autowired
	private ProjectAllocationService projectAllocationService;
	
	@Autowired
	private Environment env;

    // add new project along with mentor details
	@PostMapping(value = "/project")
    public ResponseEntity<String> allocateProject(@Valid ProjectDTO project) throws InfyInternException
    {
		Integer projId=projectAllocationService.allocateProject(project);
		String response=env.getProperty("API.ALLOCATION_SUCCESS")+":"+projId;
		return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // get mentors based on idea owner
	@GetMapping(value = "/mentor/{numberOfProjectsMentored}")
    public ResponseEntity<List<MentorDTO>> getMentors(@PathVariable("numberOfProjectsMentored") Integer numberOfProjectsMentored) throws InfyInternException
    {
		List<MentorDTO> projDTOList=projectAllocationService.getMentors(numberOfProjectsMentored);
		return new ResponseEntity<>(projDTOList, HttpStatus.OK);
    }

    // update the mentor of a project
	@PutMapping(value = "/project/{projectId}/{mentorId}")
    public ResponseEntity<String> updateProjectMentor(@PathVariable Integer projectId,
						     @PathVariable
						     @Min(value = 1000, message="{mentor.mentorid.invalid}")
						 	 @Max(value = 9999, message="{mentor.mentorid.invalid}")
						     Integer mentorId) throws InfyInternException
    {
		projectAllocationService.updateProjectMentor(projectId, mentorId);
		String response=env.getProperty("API.PROJECT_UPDATE_SUCCESS");
		return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // delete a project
	@DeleteMapping(value = "/project/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Integer projectId) throws InfyInternException
    {
		projectAllocationService.deleteProject(projectId);
		String response=env.getProperty("API.PROJECT_DELETE_SUCCESS");
		return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
