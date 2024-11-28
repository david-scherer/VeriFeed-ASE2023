package at.ac.tuwien.verifeed.core.mapper;

import at.ac.tuwien.verifeed.core.dto.PostingDto;
import at.ac.tuwien.verifeed.core.entities.Posting;

public interface PostingMapper {

    PostingDto entityToDto(Posting posting);

}
