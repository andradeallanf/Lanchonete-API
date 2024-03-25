package api.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import api.rest.model.ItemPedido;


@Repository
public interface ItemPedidoRepository extends CrudRepository< ItemPedido, Long >{

}
