package api.rest.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.rest.model.Produto;
import api.rest.repository.ProdutoRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(value = "/produto")
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Produto> getProductById(@PathVariable (value = "id") Long id) {
		Optional<Produto> produto = java.util.Optional.empty();
		
		if (!id.equals(null)) {
			produto = produtoRepository.findById(id);
		}
		
		return new ResponseEntity<Produto>(produto.get(), HttpStatus.OK);
	}
	
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Produto> insertProduct (@RequestBody Produto produto) {

		Produto produtoSalvo = produtoRepository.save(produto);
		
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}
	
	@PutMapping(value = "/{idProduto}/{preco}", produces = "application/json")
	public String updateProduct (	@PathVariable Long idProduto,	
									@PathVariable BigDecimal preco) {
		
		Optional<Produto> produto = produtoRepository.findById(idProduto);
		produto.get().setPreco(preco);
		
		produtoRepository.save(produto.get());
		
		return "Registro atualizado com sucesso";
	}
	
	@DeleteMapping(value = "/{idProduto}", produces = "application/text")
	public String deleteProduct (@PathVariable (value = "idProduto") Long id) {

		produtoRepository.deleteById(id);
		
		return "Registro removido com sucesso";
	}
}
