package uz.bookshop.mapping;

import org.mapstruct.Mapper;
import uz.bookshop.domain.dto.request_dto.PermissionRequestDTO;
import uz.bookshop.domain.dto.response_dto.PermissionResponseDTO;
import uz.bookshop.domain.model.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapping<Permission, PermissionRequestDTO, PermissionResponseDTO> {
}
