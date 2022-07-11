package com.infy.infyinterns.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.infyinterns.dto.MentorDTO;
import com.infy.infyinterns.dto.ProjectDTO;
import com.infy.infyinterns.entity.Mentor;
import com.infy.infyinterns.entity.Project;
import com.infy.infyinterns.exception.InfyInternException;
import com.infy.infyinterns.repository.MentorRepository;
import com.infy.infyinterns.repository.ProjectRepository;

@Service(value = "projectService")
@Transactional
public class ProjectAllocationServiceImpl implements ProjectAllocationService {
	
	@Autowired
	private MentorRepository mentorRepository;
	
	@Autowired
	private ProjectRepository projectRepository;

	@Override
	public Integer allocateProject(ProjectDTO project) throws InfyInternException {
		Optional<Mentor> mentorObj=mentorRepository.findById(project.getMentorDTO().getMentorId());
		Mentor mentorEntity=mentorObj.orElseThrow(()->new InfyInternException("Service.MENTOR_NOT_FOUND"));
		if(mentorEntity.getNumberOfProjectsMentored() >= 3)
			throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
		Project projObj=new Project();
		projObj.setIdeaOwner(project.getIdeaOwner());
		projObj.setProjectName(project.getProjectName());
		projObj.setReleaseDate(project.getReleaseDate());
		projObj.setMentor(mentorEntity);
		mentorEntity.setNumberOfProjectsMentored(mentorEntity.getNumberOfProjectsMentored()+1);
		return projectRepository.save(projObj).getProjectId();
	}

	
	@Override
	public List<MentorDTO> getMentors(Integer numberOfProjectsMentored) throws InfyInternException {
		List<Mentor> mentorList=mentorRepository.getMentorByNoOfProjets(numberOfProjectsMentored);
		if(mentorList.isEmpty())
			throw new InfyInternException("Service.MENTOR_NOT_FOUND");
		List<MentorDTO> mentorDTOList=new ArrayList<>();
		mentorList.forEach(x->{
			MentorDTO mentorDTO=new MentorDTO();
			mentorDTO.setMentorId(x.getMentorId());
			mentorDTO.setMentorName(x.getMentorName());
			mentorDTO.setNumberOfProjectsMentored(x.getNumberOfProjectsMentored());
			mentorDTOList.add(mentorDTO);
		});
		return mentorDTOList;
	}


	@Override
	public void updateProjectMentor(Integer projectId, Integer mentorId) throws InfyInternException {
		Optional<Mentor> mentorObj=mentorRepository.findById(projectId);
		Mentor mentorEntity=mentorObj.orElseThrow(()->new InfyInternException("Service.MENTOR_NOT_FOUND"));
		if(mentorEntity.getNumberOfProjectsMentored() >= 3)
			throw new InfyInternException("Service.CANNOT_ALLOCATE_PROJECT");
		Optional<Project> projOptionalObj=projectRepository.findById(projectId);
		Project projObj=projOptionalObj.orElseThrow(()->new InfyInternException("Service.PROJECT_NOT_FOUND"));
		projObj.setMentor(mentorEntity);
	}

	@Override
	public void deleteProject(Integer projectId) throws InfyInternException {
		Optional<Project> projOptionalObj=projectRepository.findById(projectId);
		Project projObj=projOptionalObj.orElseThrow(()->new InfyInternException("Service.PROJECT_NOT_FOUND"));
		if(projObj.getMentor() != null) {
			projObj.getMentor().setNumberOfProjectsMentored(projObj.getMentor().getNumberOfProjectsMentored()-1);
			projObj.setMentor(null);
		}
		projectRepository.delete(projObj);
	}
}