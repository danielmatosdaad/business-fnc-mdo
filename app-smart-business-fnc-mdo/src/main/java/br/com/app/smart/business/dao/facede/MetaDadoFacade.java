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
import br.com.app.smart.business.model.MetaDado;

@Stateless
public class MetaDadoFacade extends AbstractFacade<MetaDado> {

	public MetaDadoFacade() {
		super(MetaDado.class);
	}

	public MetaDadoFacade(Class<MetaDado> entityClass) {
		super(entityClass);
	}
	

	@PersistenceContext(unitName = "persistencia-contexto-funcionalidade-metadado")
	private EntityManager em;

	protected EntityManager getEntityManager() {
		return em;
	}


	public List<MetaDado> buscarTodos() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<MetaDado> criteria = cb.createQuery(MetaDado.class);
		Root<MetaDado> parametro = criteria.from(MetaDado.class);
		CriteriaQuery<MetaDado> todos = criteria.select(parametro);
		TypedQuery<MetaDado> allQuery = em.createQuery(todos);
		
		List<MetaDado> resultado = allQuery.getResultList();
		
		System.out.println("Quantidade todos? " + resultado.size());
		return resultado;
	}
}
