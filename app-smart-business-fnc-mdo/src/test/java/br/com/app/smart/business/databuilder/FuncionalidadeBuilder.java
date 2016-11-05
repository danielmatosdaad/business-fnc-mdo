package br.com.app.smart.business.databuilder;

import java.util.ArrayList;
import java.util.List;

import br.com.app.smart.business.databuilder.GrupoFuncionalidadeBuilder.GrupoTipoFuncionalidadeBuilder;
import br.com.app.smart.business.databuilder.MetaDadoBuilder.TipoMetaDadoBuilder;
import br.com.app.smart.business.funcionalidade.dto.FuncionalidadeDTO;
import br.com.app.smart.business.funcionalidade.dto.MetaDadoDTO;

public class FuncionalidadeBuilder {

	public static FuncionalidadeDTO getInstanceDTO(TipoFuncionalidadeBuilder tipo) {

		switch (tipo) {

		case INSTANCIA:
			return criarFuncionalidadeDTO();
		case FUNCIONALIDADE_METADADO:
			return criarFuncionalidadeMetaDadoDTO();

		default:
			break;
		}
		return criarFuncionalidadeDTO();
	}

	private static FuncionalidadeDTO criarFuncionalidadeDTO() {

		FuncionalidadeDTO dto = new FuncionalidadeDTO();
		dto.setNomeFuncionalidade("Manter Usuario");
		dto.setDescricao("Possibilita realizar alguma coisa");
		dto.setGrupoFuncionalidade(GrupoFuncionalidadeBuilder.getInstanceDTO(GrupoTipoFuncionalidadeBuilder.INSTANCIA));

		return dto;

	}

	private static FuncionalidadeDTO criarFuncionalidadeMetaDadoDTO() {

		FuncionalidadeDTO dto = new FuncionalidadeDTO();
		dto.setNomeFuncionalidade("Manter Usuario");
		dto.setDescricao("Possibilita realizar alguma coisa");
		dto.setGrupoFuncionalidade(GrupoFuncionalidadeBuilder.getInstanceDTO(GrupoTipoFuncionalidadeBuilder.INSTANCIA));

		List<MetaDadoDTO> listaMetadado = new ArrayList<MetaDadoDTO>();

		MetaDadoDTO mdo1 = MetaDadoBuilder.getInstanceDTO(TipoMetaDadoBuilder.INSTANCIA);
		MetaDadoDTO mdo2 = MetaDadoBuilder.getInstanceDTO(TipoMetaDadoBuilder.INSTANCIA);

		listaMetadado.add(mdo1);
		listaMetadado.add(mdo2);
		
		dto.setMetadados(listaMetadado);

		return dto;

	}

	public static enum TipoFuncionalidadeBuilder {

		INSTANCIA, FUNCIONALIDADE_METADADO;
	}
}
