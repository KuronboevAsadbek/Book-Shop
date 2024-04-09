package uz.bookshop.mapping;

import org.mapstruct.Mapper;
import uz.bookshop.domain.dto.response_dto.OrderResponseDTO;
import uz.bookshop.domain.model.Order;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    List<OrderResponseDTO> toDto(List<Order> orders);

    OrderResponseDTO toDto(Order order);
}
