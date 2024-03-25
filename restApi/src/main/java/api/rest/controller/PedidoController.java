package api.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.rest.model.Pedido;
import api.rest.service.CriarPedidoRequest;
import api.rest.service.PedidoService;
import api.rest.service.RetornoDadosPedido;

@RestController
@RequestMapping(value = "/pedido")
public class PedidoController {
	
	@Autowired
	private PedidoService pedidoService;
	

	@GetMapping(value = "/{idPedido}", produces = "application/json")
	public ResponseEntity<RetornoDadosPedido> getTotalPedido(@PathVariable (value = "idPedido") Long idPedido) {
		
		RetornoDadosPedido retorno = pedidoService.getTotalPedido(idPedido);
		
		return new ResponseEntity<RetornoDadosPedido>(retorno , HttpStatus.OK);
	}
	
	@PostMapping(value = "/{idProduto}/{quantidade}", produces = "application/json")
	public ResponseEntity<Pedido> criarPedido (
			@PathVariable (value = "idProduto") Long idProduto,
			@PathVariable (value = "quantidade") Long quantidade) {
		
		
		pedidoService.criarPedido(idProduto, quantidade);
		
		
		return new ResponseEntity<Pedido>(HttpStatus.OK);
	}
	
	@PostMapping(value = "/add-item-pedido", produces = "application/json")
	public ResponseEntity<Pedido> addItemPedido (@RequestBody CriarPedidoRequest pedidoRequest) {
		
		
		pedidoService.AdicionarItemPedido(pedidoRequest);
		
		return new ResponseEntity<Pedido>(HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/deletar-pedido", produces = "application/text")
	public String deleteItemPedido (@RequestBody CriarPedidoRequest pedidoRequest) {

		pedidoService.DeleteItemPedido(pedidoRequest);
		
		return "Item(s) removido com sucesso";
	}
}
