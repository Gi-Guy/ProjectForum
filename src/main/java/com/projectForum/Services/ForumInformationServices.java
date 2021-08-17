package com.projectForum.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projectForum.ControlPanel.Configuration.ForumInformation;
import com.projectForum.ControlPanel.Configuration.ForumInformationRepository;
import com.projectForum.Exceptions.EntityRequestException;

@Service
public class ForumInformationServices {
	
	@Autowired
	private ForumInformationRepository infoRepo;
	
	/**
	 * This method will return an ForumInformation entity 
	 * that represents the forum web application itself and some configurations*/
	public ForumInformation getForumInformation() {
		List<ForumInformation> forumInfo;
		try {
			forumInfo = infoRepo.findAll();
		} catch (Exception e) {
			throw new EntityRequestException("Could not find all forumInformation");
		}
		if(forumInfo.isEmpty() || forumInfo == null)
			return null;
		return forumInfo.get(0);
	}
	/**
	 * This method will save the new information of the forum.
	 * NOTE: If there is an exists entity, method will not save new entity.*/
	public void save(ForumInformation forumInformation) {
		ForumInformation info = this.getForumInformation();
		if(info == null || info.getId() == forumInformation.getId()) {
			try {
				infoRepo.save(forumInformation);
			} catch (Exception e) {
				throw new EntityRequestException("Could not save new forumInformation");
			}
		}
	}
	
	/**
	 * This method will update the forum Name*/
	public void updateForumName(String forumName) {
		ForumInformation forumInformation = this.getForumInformation();
		
		if(forumInformation != null) {
			forumInformation.setName(forumName);
			this.save(forumInformation);
		}
		else
			throw new EntityRequestException("Could not update forum name");
	}
	/**
	 * This method will update the forum description*/
	public void updateForumDescription(String description) {
		ForumInformation forumInformation = this.getForumInformation();
		
		if(forumInformation != null) {
			forumInformation.setDescription(description);
			this.save(forumInformation);
		}
		else
			throw new EntityRequestException("Could not update forum description");
	}
	/**
	 * This method will update the forum timeToDelete counter*/
	public void updateTimeToDelete(int timeToDelete) {
		ForumInformation forumInformation = this.getForumInformation();
		
		if(forumInformation != null) {
			forumInformation.setTimeToDelete(timeToDelete);
			this.save(forumInformation);
		}
		else
			throw new EntityRequestException("Could not update forum timeToDelete");
	}
	public void updateForumInformation(ForumInformation updatedInformation) {
		ForumInformation currentInformation = this.getForumInformation();

		//	Making sure that there is any information to edit
		if(currentInformation == null || updatedInformation == null)
			throw new EntityRequestException("Something went wrong, could not update forum information");
		
		/*
		 * updating information
		 * */
		//	Update Forum Name
		if(!currentInformation.getName().equals(updatedInformation.getName()) && !updatedInformation.getName().isBlank()) {
			this.updateForumName(updatedInformation.getName());
		}
		
		//	Update Forum Description
		if(!currentInformation.getDescription().equals(updatedInformation.getDescription()) && !updatedInformation.getDescription().isBlank()) {
			this.updateForumDescription(updatedInformation.getDescription());
		}
		
		//	Update Forum time to delete
		if(currentInformation.getTimeToDelete() != updatedInformation.getTimeToDelete() && !(updatedInformation.getTimeToDelete() < 0)) {
			this.updateTimeToDelete(updatedInformation.getTimeToDelete());
		}
		
	}
	
	/*
	 * Services for REST
	 * */
	/**
	 * This method will save and return ForumInformation object
	 * NOTE:  - user can not change the id, if user will try to change the id,
	 * 		    the services will not save the new id.
	 * 		  - This is for REST services only.*/
	public ForumInformation saveAndreturn(ForumInformation forumInformation) {
		ForumInformation info = this.getForumInformation();
		if(info == null || info.getId() == forumInformation.getId()) {
			try {
				return infoRepo.save(forumInformation);
			} catch (Exception e) {
				throw new EntityRequestException("Could not save new configurations (REST)");
			}
		}
		return null;
	}
	/**
	 * This method will update and return ForumInformation object
	 * NOTE:  - user can not change the id, if user will try to change the id,
	 * 		    the services will not save the new id.
	 * 		  - This is for REST services only.*/
	public ForumInformation updateAndReturn(ForumInformation updatedInformation) {
		ForumInformation currentInformation = this.getForumInformation();

		//	Making sure that there is any information to edit
		if(currentInformation == null || updatedInformation == null)
			throw new EntityRequestException("Something went wrong, could not update forum information (REST)");
		
		/*
		 * updating information
		 * */
		//	Update Forum's Name
		if(!currentInformation.getName().equals(updatedInformation.getName()) && !updatedInformation.getName().isBlank()) {
			currentInformation.setName(updatedInformation.getName());
		}
		
		//	Update Forum's Description
		if(!currentInformation.getDescription().equals(updatedInformation.getDescription()) && !updatedInformation.getDescription().isBlank()) {
			currentInformation.setDescription(updatedInformation.getDescription());
		}
		
		//	Update Forum's time to delete
		if(currentInformation.getTimeToDelete() != updatedInformation.getTimeToDelete() && !(updatedInformation.getTimeToDelete() < 0)) {
			currentInformation.setTimeToDelete(updatedInformation.getTimeToDelete());
		}
		return this.saveAndreturn(currentInformation);
		
	}
}
