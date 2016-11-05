package br.com.app.smart.business.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.app.smart.business.dao.interfaces.Entidade;

@Entity
@XmlRootElement
@Table(name = "grupofuncionalidade")
public class GrupoFuncionalidade implements Entidade,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 1, max = 20, message = "Tamanho maximo de caracteres sao 20")
	private String nomeGrupoFuncionalidade;

	@NotNull
	@Size(min = 1, max = 100, message = "Tamanho maximo de caracteres sao 20")
	private String descricao;

	@OneToMany(mappedBy="grupoFuncionalidade")
	private List<Funcionalidade> funcionalidades;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeGrupoFuncionalidade() {
		return nomeGrupoFuncionalidade;
	}

	public void setNomeGrupoFuncionalidade(String nomeGrupoFuncionalidade) {
		this.nomeGrupoFuncionalidade = nomeGrupoFuncionalidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Funcionalidade> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<Funcionalidade> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	
}
