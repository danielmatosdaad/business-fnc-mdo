package br.com.app.smart.business.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.app.smart.business.dao.interfaces.Entidade;

@Entity(name = "perfil")
@XmlRootElement
@Table(name = "perfil")
public class Perfil implements Entidade, Comparable<Perfil>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@Size(min = 1, max = 20, message = "Tamanho maximo de caracteres sao 20")
	private String nomePerfil;

	@NotNull
	@Size(min = 1, max = 100, message = "Tamanho maximo de caracteres sao 20")
	private String descricao;

	@OneToMany(mappedBy = "perfil")
	private List<Funcionalidade> funcionalidades;

	@OneToMany(mappedBy = "perfilPai", fetch = FetchType.LAZY)
	private List<Perfil> perfilFilhos;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "perfil_id")
	private Perfil perfilPai;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomePerfil() {
		return nomePerfil;
	}

	public void setNomePerfil(String nomePerfil) {
		this.nomePerfil = nomePerfil;
	}

	public List<Funcionalidade> getFuncionalidades() {
		return funcionalidades;
	}

	public void setFuncionalidades(List<Funcionalidade> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	public List<Perfil> getPerfilFilhos() {
		return perfilFilhos;
	}

	public void setPerfilFilhos(List<Perfil> perfilFilhos) {
		this.perfilFilhos = perfilFilhos;
	}

	public Perfil getPerfilPai() {
		return perfilPai;
	}

	public void setPerfilPai(Perfil perfilPai) {
		this.perfilPai = perfilPai;
	}

	@Override
	public int compareTo(Perfil o) {

		if (o.id == this.id) {
			return 0;
		} else if (o.id > this.id) {
			return 1;
		} else {
			return -1;
		}

	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Perfil)) {

			return false;
		}

		Perfil perfil = (Perfil) obj;

		if (super.equals(perfil) && perfil.id != this.id) {

			return false;
		}

		return true;
	}

}
