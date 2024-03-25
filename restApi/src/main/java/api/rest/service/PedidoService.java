package api.rest.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import api.rest.model.ItemPedido;
import api.rest.model.Pedido;
import api.rest.model.Produto;
import api.rest.repository.PedidoRepository;
import api.rest.repository.ItemPedidoRepository;
import api.rest.repository.ProdutoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
    @PersistenceContext
    private EntityManager manager;
	
	
	private final String ABERTO = "Aberto";
	
	String sumQtItens = "select max(i.qtProduto) from ItemPedido i where i.pedido.idPedido = :idPedido and i.produto.idProduto = :idProduto";
	
	String IdItem = "select max(idItemPedido) from ItemPedido i where i.pedido.idPedido = :idPedido and i.produto.idProduto = :idProduto";
	
	String deleteItens = "delete ItemPedido i where i.pedido.idPedido = :idPedido and i.produto.idProduto = :idProduto";
	
	String sumTotal = 
				" select sum(b.preco * a.qtProduto) " +
				" from   ItemPedido a," +
				" 		 Produto b" +
			 	" where  a.produto.idProduto = b.idProduto " +
			 	" and    a.pedido.idPedido = :idPedido ";


	public void criarPedido(Long idProduto, Long quantidade) {		
		
		Optional<Produto> produto = produtoRepository.findById(idProduto);

	   	Pedido pedido = new Pedido();
	   	ItemPedido item = new ItemPedido();
        item.setProduto(produto.get());
        item.setQtProduto(quantidade);
        item.setPedido(pedido);
   
        pedido.getItens().add(item);
        pedido.setStatusPedido(ABERTO);

        pedidoRepository.save(pedido);
		
	}
	
	public void AdicionarItemPedido(CriarPedidoRequest pedidoRequest) {		
		ItemPedido item = new ItemPedido();
		Long resultado = (long) 0;
		
		Produto produto = produtoRepository.findById(pedidoRequest.getIdProduto()).
		orElseThrow(() -> new  IllegalArgumentException("Código de produto não encontrado"));
		
		Pedido pedido = pedidoRepository.findById(pedidoRequest.getIdPedido()).
		orElseThrow(() -> new  IllegalArgumentException("Pedido não encontrado"));
		
		TypedQuery<ItemPedido> query = buildSQL(pedidoRequest, IdItem);
		
		try {
			resultado = (long) Integer.parseInt(String.valueOf(query.getSingleResult()));
		} catch (Exception e) {
			resultado = (long) 0;
		}
		
		if (resultado > 1) {
			item.setIdItemPedido(resultado);
			query = buildSQL(pedidoRequest, sumQtItens);
			resultado = (long) Integer.parseInt(String.valueOf(query.getSingleResult()));
		}

        item.setProduto(produto);
        item.setQtProduto(pedidoRequest.getQuantidade() + resultado);
        item.setPedido(pedido);

        itemPedidoRepository.save(item);
		
	}
	
	@Transactional
	public void DeleteItemPedido(CriarPedidoRequest pedidoRequest) {      
        
		TypedQuery<ItemPedido> query = buildSQL(pedidoRequest, sumQtItens);
        
		int resultado = Integer.parseInt(String.valueOf(query.getSingleResult()));
		
		if(resultado <= pedidoRequest.getQuantidade()) {
			manager.createQuery(deleteItens)
            .setParameter("idPedido", pedidoRequest.getIdPedido())
            .setParameter("idProduto", pedidoRequest.getIdProduto())
            .executeUpdate();			
		}
		else if (resultado > pedidoRequest.getQuantidade()) {
			AtualizarQtItens(resultado, pedidoRequest);
		} 
	}
	
	public void AtualizarQtItens(Integer resultado, CriarPedidoRequest pedidoRequest) {
		Long updatedQtItens = (Long) (resultado - pedidoRequest.getQuantidade());
		
		pedidoRequest.setQuantidade(updatedQtItens);
		pedidoRequest.setIdPedido(pedidoRequest.getIdPedido());
		pedidoRequest.setIdProduto(pedidoRequest.getIdProduto());
		
		Produto produto = produtoRepository.findById(pedidoRequest.getIdProduto()).
		orElseThrow(() -> new  IllegalArgumentException("Código de produto não encontrado"));
		
		Pedido pedido = pedidoRepository.findById(pedidoRequest.getIdPedido()).
		orElseThrow(() -> new  IllegalArgumentException("Pedido não encontrado"));
		
		TypedQuery<ItemPedido> query = buildSQL(pedidoRequest, IdItem);
		Long idItem = (long) Integer.parseInt(String.valueOf(query.getSingleResult()));
		
		ItemPedido item = new ItemPedido();
		item.setIdItemPedido(idItem);
		item.setProduto(produto);
        item.setQtProduto(pedidoRequest.getQuantidade());
        item.setPedido(pedido);

        itemPedidoRepository.save(item);
	}
	
	public TypedQuery<ItemPedido> buildSQL(CriarPedidoRequest pedidoRequest, String sql) {
		TypedQuery<ItemPedido> query = manager.createQuery(sql, ItemPedido.class);
		query.setParameter("idPedido", pedidoRequest.getIdPedido());
		query.setParameter("idProduto", pedidoRequest.getIdProduto());
		return query;
	}
	
	public RetornoDadosPedido getTotalPedido(Long idPedido) {
		RetornoDadosPedido retorno = new RetornoDadosPedido();
		TypedQuery<ItemPedido> query = manager.createQuery(sumTotal, ItemPedido.class);
		query.setParameter("idPedido", idPedido);
		
		BigDecimal total =  new BigDecimal(String.valueOf(query.getSingleResult()));;
		
		retorno.setTotalPedido(total);
		retorno.setIdPedido(idPedido);
	
		return retorno;
		
	}
}
