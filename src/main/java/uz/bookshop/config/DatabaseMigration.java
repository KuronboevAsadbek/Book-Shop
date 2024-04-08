package uz.bookshop.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.bookshop.repository.RoleRepository;
import uz.bookshop.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseMigration {
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final EntityManager entityManager;

//    @Bean
//    @Transactional
//    public void run() {
//        String inserts = "INSERT INTO permission (id, name) VALUES (1, 'FULL_ACCESS');" +
//                "INSERT INTO permission (id, name) VALUES (2, 'ADMIN_ACCESS');" +
//                "INSERT INTO permission (id, name) VALUES (3, 'USER_ACCESS');" +
//                "INSERT INTO permission (id, name) VALUES (4, 'AUTHOR_ACCESS');" +
//                "INSERT INTO permission (id, name) VALUES (5, 'MANAGER_ACCESS');";
//
//        try {
//            entityManager.createNativeQuery(inserts).executeUpdate();
//            roleRepository.save(
//                    Role
//                            .builder()
//                            .name("ROLE_SUPER_ADMIN")
//                            .permissions(
//                                    Set.of(
//                                            Permission.builder().id(1L).build(),
//                                            Permission.builder().id(2L).build(),
//                                            Permission.builder().id(3L).build(),
//                                            Permission.builder().id(4L).build(),
//                                            Permission.builder().id(5L).build()
//
//                                    )
//                            ).build()
//            );
//
//            roleRepository.save(
//                    Role
//                            .builder()
//                            .name("ROLE_ADMIN")
//                            .permissions(
//                                    Set.of(
//                                            Permission.builder().id(2L).build(),
//                                            Permission.builder().id(3L).build(),
//                                            Permission.builder().id(4L).build(),
//                                            Permission.builder().id(5L).build()
//                                    )
//                            ).build()
//            );
//
//            userService.createUser(
//                    UserRequestDto
//                            .builder()
//                            .firstName("Super")
//                            .lastName("Admin")
//                            .username("superadmin")
//                            .password("1234")
//                            .roles(
//                                    Set.of(
//                                            RoleResponseDTO
//                                                    .builder()
//                                                    .id(1L)
//                                                    .build()
//                                    )
//                            )
//                            .build()
//            );
//
//
//        } catch (Exception e) {
//            log.error("Error while inserting permissions", e);
//        }
//    }
}
