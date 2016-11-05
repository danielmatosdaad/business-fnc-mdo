package br.com.app.smart.business.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import br.appsmartbusiness.persistencia.service.ServiceDAO;
import br.com.app.smart.business.dao.facede.FuncionalidadeFacade;
import br.com.app.smart.business.dao.interfaces.IServicoLocalDAO;
import br.com.app.smart.business.dao.interfaces.IServicoRemoteDAO;
import br.com.app.smart.business.exception.InfraEstruturaException;
import br.com.app.smart.business.exception.NegocioException;
import br.com.app.smart.business.funcionalidade.dto.FuncionalidadeDTO;
import br.com.app.smart.business.funcionalidade.dto.GrupoFuncionalidadeDTO;
import br.com.app.smart.business.model.Funcionalidade;

@Stateless(mappedName = "FuncionalidadeServiceImp")
@Remote(value = { IServicoRemoteDAO.class })
@Local(value = { IServicoLocalDAO.class })
public class FuncionalidadeServiceImp
		implements IServicoRemoteDAO<FuncionalidadeDTO>, IServicoLocalDAO<FuncionalidadeDTO> {


	@EJB(beanName = "GrupoFuncionalidadeServiceImp", beanInterface = IServicoLocalDAO.class)
	private IServicoLocalDAO<GrupoFuncionalidadeDTO> grupoFuncionalidadeService;

	@EJB
	private FuncionalidadeFacade funcionalidadeFacede;

	@Override
	public FuncionalidadeDTO adiconar(FuncionalidadeDTO dto) throws InfraEstruturaException, NegocioException {

		try {

			if (dto.getGrupoFuncionalidade() == null) {

				throw new NegocioException(-1, "Obrigato uma funcionalidade participar de um grupo");
			}

			this.grupoFuncionalidadeService.adiconar(dto.getGrupoFuncionalidade());

			ServiceDAO.adiconar(funcionalidadeFacede, Funcionalidade.class, dto);

			return dto;

		} catch (Exception e) {
			e.printStackTrace();
			throw new InfraEstruturaException(e);

		}
	}

	@Override
	public List<FuncionalidadeDTO> adiconar(List<FuncionalidadeDTO> lista)
			throws InfraEstruturaException, NegocioException {

		try {

			for (FuncionalidadeDTO FuncionalidadeDTO : lista) {

				adiconar(FuncionalidadeDTO);
			}

			return lista;

		} catch (Exception e) {
			throw new InfraEstruturaException(e);

		}

	}

	@Override
	public FuncionalidadeDTO bustarPorID(Long id) throws InfraEstruturaException, NegocioException {
		try {

			return ServiceDAO.bustarPorID(this.funcionalidadeFacede, FuncionalidadeDTO.class, id);
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

	public void remover(FuncionalidadeDTO dto) throws InfraEstruturaException, NegocioException {
		try {
			ServiceDAO.remover(this.funcionalidadeFacede, Funcionalidade.class, dto);

		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

	public FuncionalidadeDTO alterar(FuncionalidadeDTO dto) throws InfraEstruturaException, NegocioException {
		try {
			ServiceDAO.alterar(this.funcionalidadeFacede, Funcionalidade.class, dto);
			return dto;
		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}

	}

	@Override
	public List<FuncionalidadeDTO> bustarTodos() throws InfraEstruturaException, NegocioException {
		try {

			return ServiceDAO.bustarTodos(this.funcionalidadeFacede, FuncionalidadeDTO.class);
		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

	@Override
	public List<FuncionalidadeDTO> bustarPorIntervaloID(int[] range) throws InfraEstruturaException, NegocioException {
		try {

			return ServiceDAO.bustarPorIntervaloID(this.funcionalidadeFacede, FuncionalidadeDTO.class, range);
		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

}
