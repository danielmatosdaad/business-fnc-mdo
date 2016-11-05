
package br.com.app.smart.business.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.app.smart.business.dao.interfaces.Entidade;

@Entity
@XmlRootElement
@Table(name = "funcionalidade")
public class Funcionalidade implements Entidade, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 1, max = 20, message = "Tamanho maximo de caracteres sao 20")
	private String nomeFuncionalidade;

	@NotNull
	@Size(min = 1, max = 100, message = "Tamanho maximo de caracteres sao 20")
	private String descricao;

	@ManyToOne
	private Perfil perfil;

	@ManyToOne
	private GrupoFuncionalidade grupoFuncionalidade;

	@OneToMany(mappedBy = "funcionalidade")
	private List<MetaDado> metadados;
	
	
	@OneToMany(mappedBy = "funcionalidadePai", fetch = FetchType.LAZY)
	private List<Funcionalidade> funcionalidadeFilhos;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "funcionalidade_id")
	private Funcionalidade funcionalidadePai;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeFuncionalidade() {
		return nomeFuncionalidade;
	}

	public void setNomeFuncionalidade(String nomeFuncionalidade) {
		this.nomeFuncionalidade = nomeFuncionalidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public GrupoFuncionalidade getGrupoFuncionalidade() {
		return grupoFuncionalidade;
	}

	public void setGrupoFuncionalidade(GrupoFuncionalidade grupoFuncionalidade) {
		this.grupoFuncionalidade = grupoFuncionalidade;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public List<MetaDado> getMetadados() {
		return metadados;
	}

	public void setMetadados(List<MetaDado> metadados) {
		this.metadados = metadados;
	}

	public List<Funcionalidade> getFuncionalidadeFilhos() {
		return funcionalidadeFilhos;
	}

	public void setFuncionalidadeFilhos(List<Funcionalidade> funcionalidadeFilhos) {
		this.funcionalidadeFilhos = funcionalidadeFilhos;
	}

	public Funcionalidade getFuncionalidadePai() {
		return funcionalidadePai;
	}

	public void setFuncionalidadePai(Funcionalidade funcionalidadePai) {
		this.funcionalidadePai = funcionalidadePai;
	}
	
	

}
