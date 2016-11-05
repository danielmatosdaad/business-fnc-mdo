package br.com.app.smart.business.service;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import br.appsmartbusiness.persistencia.service.ServiceDAO;
import br.com.app.smart.business.dao.facede.PerfilFacade;
import br.com.app.smart.business.dao.interfaces.IServicoLocalDAO;
import br.com.app.smart.business.dao.interfaces.IServicoRemoteDAO;
import br.com.app.smart.business.exception.InfraEstruturaException;
import br.com.app.smart.business.exception.NegocioException;
import br.com.app.smart.business.funcionalidade.dto.FuncionalidadeDTO;
import br.com.app.smart.business.funcionalidade.dto.PerfilDTO;
import br.com.app.smart.business.model.Perfil;

@Stateless(mappedName = "PerfilServiceImp")
@Remote(value = { IServicoRemoteDAO.class })
@Local(value = { IServicoLocalDAO.class })
public class PerfilServiceImp implements IServicoRemoteDAO<PerfilDTO>, IServicoLocalDAO<PerfilDTO> {


	@EJB(beanName = "FuncionalidadeServiceImp", beanInterface = IServicoLocalDAO.class)
	private IServicoLocalDAO<FuncionalidadeDTO> funcionalidadeServiceImp;

	@EJB
	private PerfilFacade perfilFacade;

	@Override
	public PerfilDTO adiconar(PerfilDTO dto) throws InfraEstruturaException, NegocioException {

		try {

			ServiceDAO.adiconar(perfilFacade, Perfil.class, dto);

			return dto;

		} catch (Exception e) {
			e.printStackTrace();
			throw new InfraEstruturaException(e);

		}
	}

	@Override
	public List<PerfilDTO> adiconar(List<PerfilDTO> lista) throws InfraEstruturaException, NegocioException {

		try {

			for (PerfilDTO PerfilDTO : lista) {

				adiconar(PerfilDTO);
			}

			return lista;

		} catch (Exception e) {
			throw new InfraEstruturaException(e);

		}

	}

	@Override
	public PerfilDTO bustarPorID(Long id) throws InfraEstruturaException, NegocioException {
		try {

			return ServiceDAO.bustarPorID(this.perfilFacade, PerfilDTO.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InfraEstruturaException(e);
		}
	}

	public void removerPorId(Long id) throws InfraEstruturaException, NegocioException {
		try {
		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

	public void remover(PerfilDTO dto) throws InfraEstruturaException, NegocioException {
		try {
			ServiceDAO.remover(this.perfilFacade, Perfil.class, dto);

		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

	public PerfilDTO alterar(PerfilDTO dto) throws InfraEstruturaException, NegocioException {
		try {
			ServiceDAO.alterar(this.perfilFacade, Perfil.class, dto);
			return dto;
		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}

	}

	@Override
	public List<PerfilDTO> bustarTodos() throws InfraEstruturaException, NegocioException {
		try {

			return ServiceDAO.bustarTodos(this.perfilFacade, PerfilDTO.class);
		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

	@Override
	public List<PerfilDTO> bustarPorIntervaloID(int[] range) throws InfraEstruturaException, NegocioException {
		try {

			return ServiceDAO.bustarPorIntervaloID(this.perfilFacade, PerfilDTO.class, range);
		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

}
