package uz.bookshop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.bookshop.domain.dto.request_dto.RoleRequestDTO;
import uz.bookshop.domain.dto.response_dto.RoleResponseDTO;
import uz.bookshop.mapping.RoleMapper;
import uz.bookshop.repository.RoleRepository;
import uz.bookshop.service.RoleService;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO) {
        return roleMapper.toDto(roleRepository.save(roleMapper.toEntity(roleRequestDTO)));
    }
}
