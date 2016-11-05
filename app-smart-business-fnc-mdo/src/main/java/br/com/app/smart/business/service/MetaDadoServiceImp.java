package br.com.app.smart.business.service;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import br.appsmartbusiness.persistencia.service.ServiceDAO;
import br.com.app.smart.business.dao.facede.MetaDadoFacade;
import br.com.app.smart.business.dao.interfaces.IServicoLocalDAO;
import br.com.app.smart.business.dao.interfaces.IServicoRemoteDAO;
import br.com.app.smart.business.exception.InfraEstruturaException;
import br.com.app.smart.business.exception.NegocioException;
import br.com.app.smart.business.funcionalidade.dto.MetaDadoDTO;
import br.com.app.smart.business.model.MetaDado;

@Stateless
@Remote(value = { IServicoRemoteDAO.class })
@Local(value = { IServicoLocalDAO.class })
public class MetaDadoServiceImp implements IServicoRemoteDAO<MetaDadoDTO>, IServicoLocalDAO<MetaDadoDTO> {


	@EJB
	private MetaDadoFacade metaDadoFacade;
	

	@Override
	public MetaDadoDTO adiconar(MetaDadoDTO dto) throws InfraEstruturaException, NegocioException {

		try {
			return ServiceDAO.adiconar(this.metaDadoFacade, MetaDado.class, dto);

		} catch (Exception e) {
			throw new InfraEstruturaException(e);

		}
	}

	@Override
	public List<MetaDadoDTO> adiconar(List<MetaDadoDTO> listaDto) throws InfraEstruturaException, NegocioException {

		for (MetaDadoDTO MetaDadoDTO : listaDto) {
			adiconar(MetaDadoDTO);
		}
		return listaDto;
	}

	@Override
	public MetaDadoDTO bustarPorID(Long id) throws InfraEstruturaException, NegocioException {
		try {
			return ServiceDAO.bustarPorID(this.metaDadoFacade, MetaDadoDTO.class, id);

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

	public void remover(MetaDadoDTO dto) throws InfraEstruturaException, NegocioException {
		try {

			ServiceDAO.remover(metaDadoFacade, MetaDado.class, dto);


		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

	public MetaDadoDTO alterar(MetaDadoDTO dto) throws InfraEstruturaException, NegocioException {
		try {

			return ServiceDAO.alterar(metaDadoFacade, MetaDado.class, dto);

		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}

	}

	@Override
	public List<MetaDadoDTO> bustarTodos() throws InfraEstruturaException, NegocioException {
		try {
			return ServiceDAO.bustarTodos(this.metaDadoFacade, MetaDadoDTO.class);

		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}

	@Override
	public List<MetaDadoDTO> bustarPorIntervaloID(int[] range) throws InfraEstruturaException, NegocioException {
		try {

			return ServiceDAO.bustarPorIntervaloID(this.metaDadoFacade, MetaDadoDTO.class, range);

		} catch (Exception e) {
			throw new InfraEstruturaException(e);
		}
	}
}
