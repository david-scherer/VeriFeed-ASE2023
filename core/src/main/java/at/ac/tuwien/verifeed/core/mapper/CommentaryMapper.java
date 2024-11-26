package at.ac.tuwien.verifeed.core.mapper;

import at.ac.tuwien.verifeed.core.dto.CommentaryDto;
import at.ac.tuwien.verifeed.core.entities.Commentary;

public interface CommentaryMapper {

    CommentaryDto entityToDto(Commentary commentary);

}
