package com.epam.esm.service.mapper;

import com.epam.esm.model.DTO.tag.CreateTagRequest;
import com.epam.esm.model.DTO.tag.TagDTO;
import com.epam.esm.model.entity.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {
    private final ModelMapper mapper = new ModelMapper();
    public TagDTO toDTO (Tag tag){
        return mapper.map(tag, TagDTO.class);
    }

    public Tag toTag (CreateTagRequest createTagRequest){
        return mapper.map(createTagRequest, Tag.class);
    }
}
