package api.rest.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ItemPedido implements Serializable{

	private static final long serialVersionUID = 1L;
	   	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idItemPedido;
		   
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "id_pedido", referencedColumnName = "idPedido")
	private Pedido pedido; 
	
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "id_produto", referencedColumnName = "idProduto")
    private Produto produto;
    
    
    private Long qtProduto;

	public Long getIdItemPedido() {
		return idItemPedido;
	}

	public void setIdItemPedido(Long idItemPedido) {
		this.idItemPedido = idItemPedido;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Long getQtProduto() {
		return qtProduto;
	}

	public void setQtProduto(Long quantidade) {
		this.qtProduto = quantidade;
	}
    
    

    
}
