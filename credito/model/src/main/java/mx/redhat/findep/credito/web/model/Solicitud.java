package mx.redhat.findep.credito.web.model;

import java.io.Serializable;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement
public class Solicitud implements Serializable
{
	private Long id;
	private Set<Cliente> clientes;
	private Double monto;
	private String resultado;
	private String nombreGrupo;
	private String tipoRevision;
	private String estatusSolicitud;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set<Cliente> getClientes() {
		return clientes;
	}
	public void setClientes(Set<Cliente> clientes) {
		this.clientes = clientes;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public String getNombreGrupo() {
		return nombreGrupo;
	}
	public void setNombreGrupo(String nombreGrupo) {
		this.nombreGrupo = nombreGrupo;
	}
	public String getTipoRevision() {
		return tipoRevision;
	}
	public void setTipoRevision(String tipoRevision) {
		this.tipoRevision = tipoRevision;
	}
	public String getEstatusSolicitud() {
		return estatusSolicitud;
	}
	public void setEstatusSolicitud(String estatusSolicitud) {
		this.estatusSolicitud = estatusSolicitud;
	}

}
