package uz.bookshop.mapping;

import org.mapstruct.Mapper;
import uz.bookshop.domain.dto.request_dto.RoleRequestDTO;
import uz.bookshop.domain.dto.response_dto.RoleResponseDTO;
import uz.bookshop.domain.model.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapping<Role, RoleRequestDTO, RoleResponseDTO> {
}
