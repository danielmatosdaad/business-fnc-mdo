package br.com.app.smart.business.databuilder;


import br.com.app.smart.business.funcionalidade.dto.GrupoFuncionalidadeDTO;

public class GrupoFuncionalidadeBuilder {

	public static GrupoFuncionalidadeDTO getInstanceDTO(GrupoTipoFuncionalidadeBuilder tipo) {

		switch (tipo) {

		case INSTANCIA:
			return criarGrupoFuncionalidadeDTO();

		default:
			break;
		}
		return criarGrupoFuncionalidadeDTO();
	}

	private static GrupoFuncionalidadeDTO criarGrupoFuncionalidadeDTO() {

		GrupoFuncionalidadeDTO dto = new GrupoFuncionalidadeDTO();
		dto.setNomeGrupoFuncionalidade("Usuario");
		dto.setDescricao("Possibilita realizar alguma coisa");
		return dto;

	}

	public static enum GrupoTipoFuncionalidadeBuilder {

		INSTANCIA;
	}
}
