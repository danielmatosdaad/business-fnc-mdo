package br.com.app.smart.business.dao.facede;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.appsmartbusiness.persistencia.dao.facede.AbstractFacade;
import br.com.app.smart.business.model.GrupoFuncionalidade;

@Stateless
public class GrupoFuncionalidadeFacade extends AbstractFacade<GrupoFuncionalidade> {

	public GrupoFuncionalidadeFacade() {
		super(GrupoFuncionalidade.class);
	}

	public GrupoFuncionalidadeFacade(Class<GrupoFuncionalidade> entityClass) {
		super(entityClass);
	}
	

	@PersistenceContext(unitName = "persistencia-contexto-funcionalidade-metadado")
	private EntityManager em;

	protected EntityManager getEntityManager() {
		return em;
	}


	public List<GrupoFuncionalidade> buscarTodos() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GrupoFuncionalidade> criteria = cb.createQuery(GrupoFuncionalidade.class);
		Root<GrupoFuncionalidade> root = criteria.from(GrupoFuncionalidade.class);
		CriteriaQuery<GrupoFuncionalidade> todos = criteria.select(root);
		TypedQuery<GrupoFuncionalidade> allQuery = em.createQuery(todos);
		
		List<GrupoFuncionalidade> resultado = allQuery.getResultList();
		
		System.out.println("Quantidade todos? " + resultado.size());
		return resultado;
	}
}
