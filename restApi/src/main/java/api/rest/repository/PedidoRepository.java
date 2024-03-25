package api.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import api.rest.model.Pedido;


@Repository
public interface PedidoRepository extends CrudRepository< Pedido, Long >{

}
