package uz.bookshop.service;

import uz.bookshop.domain.dto.request_dto.RoleRequestDTO;
import uz.bookshop.domain.dto.response_dto.RoleResponseDTO;

public interface RoleService {
    RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO);
}
