package api.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import api.rest.model.Produto;

@Repository
public interface ProdutoRepository extends CrudRepository< Produto, Long >{

}
