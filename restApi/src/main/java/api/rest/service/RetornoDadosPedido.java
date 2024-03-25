package api.rest.service;

import java.math.BigDecimal;

public class RetornoDadosPedido {
	
    private Long idPedido;
    
    private BigDecimal totalPedido;

	public Long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public BigDecimal getTotalPedido() {
		return totalPedido;
	}

	public void setTotalPedido(BigDecimal totalPedido) {
		this.totalPedido = totalPedido;
	}
    
    

}
