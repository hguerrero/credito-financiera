package mx.redhat.findep.credito.web.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@SuppressWarnings("serial")
public class Cliente implements Serializable 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cliente_id")
	private Long id;
	private String cliente;
	private String nombre;
	private Double monto;
	
	@JsonIgnore
	@XmlTransient
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "solicitud_id", nullable = false)
	private Solicitud solicitud;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Solicitud getSolicitud() {
		return solicitud;
	}
	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}

	public void afterUnmarshal(Unmarshaller unmarshaller, Object parent){
		if (parent instanceof Solicitud) {
			Solicitud solicitud = (Solicitud) parent;
			this.solicitud = (Solicitud)solicitud;			
		}
	}
}
