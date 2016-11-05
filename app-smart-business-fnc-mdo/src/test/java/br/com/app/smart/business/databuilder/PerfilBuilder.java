package br.com.app.smart.business.databuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.app.smart.business.databuilder.FuncionalidadeBuilder.TipoFuncionalidadeBuilder;
import br.com.app.smart.business.dto.RegistroAuditoriaDTO;
import br.com.app.smart.business.funcionalidade.dto.FuncionalidadeDTO;
import br.com.app.smart.business.funcionalidade.dto.PerfilDTO;

public class PerfilBuilder {

	public static PerfilDTO getInstanceDTO(TipoPerfilBuilder tipo) {

		switch (tipo) {

		case INSTANCIA:
			return criarPerfilDTO();

		default:
			break;
		}
		return criarPerfilDTO();
	}

	private static PerfilDTO criarPerfilDTO() {
		RegistroAuditoriaDTO r = new RegistroAuditoriaDTO();
		r.setDataCadastro(new Date());

		PerfilDTO dto = new PerfilDTO();
		dto.setNomePerfil("nomePerfil");
		dto.setDescricao("Descricao Perfil");
		
		return dto;

	}

	private static PerfilDTO criarPerfilFuncionalidadeDTO() {
		RegistroAuditoriaDTO r = new RegistroAuditoriaDTO();
		r.setDataCadastro(new Date());

		PerfilDTO dto = new PerfilDTO();
		dto.setNomePerfil("nomePerfil");
		dto.setDescricao("Descricao Perfil");
		
		List<FuncionalidadeDTO> funcionalidades = new ArrayList<FuncionalidadeDTO>();
		funcionalidades.add(FuncionalidadeBuilder.getInstanceDTO(TipoFuncionalidadeBuilder.INSTANCIA));
		funcionalidades.add(FuncionalidadeBuilder.getInstanceDTO(TipoFuncionalidadeBuilder.INSTANCIA));
		dto.setFuncionalidades(funcionalidades);
		return dto;

	}
	public static enum TipoPerfilBuilder {

		INSTANCIA;
	}
}
