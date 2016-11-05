package br.com.app.smart.business.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import br.appsmartbusiness.persistencia.service.ServiceDAO;
import br.com.app.smart.business.dao.facede.GrupoFuncionalidadeFacade;
import br.com.app.smart.business.dao.interfaces.IServicoLocalDAO;
import br.com.app.smart.business.dao.interfaces.IServicoRemoteDAO;
import br.com.app.smart.business.exception.InfraEstruturaException;
import br.com.app.smart.business.exception.NegocioException;
import br.com.app.smart.business.funcionalidade.dto.GrupoFuncionalidadeDTO;
import br.com.app.smart.business.model.GrupoFuncionalidade;

@Stateless(mappedName = "GrupoFuncionalidadeServiceImp")
@Remote(value = { IServicoRemoteDAO.class })
@Local(value = { IServicoLocalDAO.class })
public class GrupoFuncionalidadeServiceImp
		implements IServicoRemoteDAO<GrupoFuncionalidadeDTO>, IServicoLocalDAO<GrupoFuncionalidadeDTO> {


	@EJB
	GrupoFuncionalidadeFacade grupoFuncionalidadeFacede;

	@Override
	public GrupoFuncionalidadeDTO adiconar(GrupoFuncionalidadeDTO dto)
			throws InfraEstruturaException, NegocioException {

		try {

			ServiceDAO.adiconar(grupoFuncionalidadeFacede, GrupoFuncionalidade.class, dto);

			return dto;

		} catch (Exception e) {
			e.printStackTrace();
			throw new InfraEstruturaException(e);

		}
	}

	@Override
	public List<GrupoFuncionalidadeDTO> adiconar(List<GrupoFuncionalidadeDTO> lista)
			throws InfraEstruturaException, NegocioException {

		try {

			for (GrupoFuncionalidadeDTO GrupoFuncionalidadeDTO : lista) {

				adiconar(GrupoFuncionalidadeDTO);
			}

			return lista;

		} catch (Exception e) {
			throw new InfraEstruturaException(e);

		}

	}

	@Override
	public GrupoFuncionalidadeDTO bustarPorID(Long id) throws InfraEstruturaException, NegocioException {
		try {

			return ServiceDAO.bustarPorID(this.grupoFuncionalidadeFacede, GrupoFuncionalidadeDTO.class, id);
		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

	public void removerPorId(Long id) throws InfraEstruturaException, NegocioException {
		try {
		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

	public void remover(GrupoFuncionalidadeDTO dto) throws InfraEstruturaException, NegocioException {
		try {
			ServiceDAO.remover(this.grupoFuncionalidadeFacede, GrupoFuncionalidade.class, dto);

		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

	public GrupoFuncionalidadeDTO alterar(GrupoFuncionalidadeDTO dto) throws InfraEstruturaException, NegocioException {
		try {

			ServiceDAO.alterar(this.grupoFuncionalidadeFacede, GrupoFuncionalidade.class, dto);
			return dto;

		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}

	}

	@Override
	public List<GrupoFuncionalidadeDTO> bustarTodos() throws InfraEstruturaException, NegocioException {
		try {

			return ServiceDAO.bustarTodos(this.grupoFuncionalidadeFacede, GrupoFuncionalidadeDTO.class);
		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

	@Override
	public List<GrupoFuncionalidadeDTO> bustarPorIntervaloID(int[] range)
			throws InfraEstruturaException, NegocioException {
		try {

			return ServiceDAO.bustarPorIntervaloID(this.grupoFuncionalidadeFacede, GrupoFuncionalidadeDTO.class, range);
		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

}
