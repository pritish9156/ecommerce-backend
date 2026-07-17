package service;

import java.util.ArrayList;
import java.util.List;

import dao.TagDAO;
import dto.ApiResponse;
import dto.CategoryRequestDTO;
import dto.CategoryResponseDTO;
import dto.TagRequestDTO;
import dto.TagResponseDTO;
import entity.Category;
import entity.Tags;

public class TagService {

	TagDAO tagDAO;
	
	public TagService(){
		tagDAO = new TagDAO();
	}
	
	public ApiResponse createTag(TagRequestDTO dto) {

		Tags tag = tagDAO.findByName(dto.getName());

		if (tag != null) {
			
			if(!tag.isActive()) {
				tag.setDescription(dto.getDescription());			
				tag.setActive(true);
				
				Boolean status = tagDAO.update(tag);
				
				if(status)	
					return new ApiResponse(true, "tag added successfully.");
				else
					return new ApiResponse(false, "tag failed to add");
			}
			
			return new ApiResponse(false, "tag already exists.");
			
		}

		tag = new Tags();

		tag.setName(dto.getName());
		tag.setDescription(dto.getDescription());

		boolean saveStatus = tagDAO.save(tag);

		if (saveStatus)
			return new ApiResponse(true, "tag added successfully.");
		else
			return new ApiResponse(false, "tag failed to add");

	}

	public ApiResponse updateTag(TagRequestDTO dto) {

		Tags tag = tagDAO.findById(dto.getId());

		if (tag == null)
			return new ApiResponse(false, "tag not found.");

		Tags existingTag = tagDAO.findByName(dto.getName());

		if (existingTag != null && !existingTag.getId().equals(tag.getId()))
			return new ApiResponse(false, "tag already exists.");

		tag.setName(dto.getName());

		tag.setDescription(dto.getDescription());

		tagDAO.update(tag);

		return new ApiResponse(true, "tag updated successfully.");

	}

	public ApiResponse getAllTags() {

		List<Tags> tagDetails = tagDAO.findAll();
		
		List<TagResponseDTO> tagList = new ArrayList<>();
		
		for(Tags tag : tagDetails) {
			
			TagResponseDTO dto = new TagResponseDTO();
			
			dto.setName(tag.getName());
			dto.setDescription(tag.getDescription());
			dto.setId(tag.getId());
			
			if(tag.isActive())
				tagList.add(dto);
		}

		return new ApiResponse(true, "Fetched successfully", tagList);
	}

	public ApiResponse deactivateTag(Long id) {

		Tags tag = tagDAO.findById(id);

		if (tag == null)
			return new ApiResponse(false, "tag not found.");

		tag.setActive(false);

		boolean updateStatus = tagDAO.update(tag);

		if (updateStatus)
			return new ApiResponse(true, "tag deleted");
		else
			return new ApiResponse(false, "failed to delete tag");

	}
}
