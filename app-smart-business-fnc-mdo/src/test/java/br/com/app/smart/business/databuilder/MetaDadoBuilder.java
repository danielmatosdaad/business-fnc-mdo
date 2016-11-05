package br.com.app.smart.business.databuilder;

import java.util.Date;

import br.com.app.smart.business.dto.RegistroAuditoriaDTO;
import br.com.app.smart.business.funcionalidade.dto.MetaDadoDTO;

public class MetaDadoBuilder {

	public static MetaDadoDTO getInstanceDTO(TipoMetaDadoBuilder tipo) {

		switch (tipo) {

		case INSTANCIA:
			return criarMetaDadoDTO();

		default:
			break;
		}
		return criarMetaDadoDTO();
	}

	private static MetaDadoDTO criarMetaDadoDTO() {
		RegistroAuditoriaDTO r = new RegistroAuditoriaDTO();
		r.setDataCadastro(new Date());

		MetaDadoDTO dto = new MetaDadoDTO();
		dto.setDescricaoTela("descricao tela");
		dto.setNumeroTela(1);
		dto.setTituloTela("TituloTela");
		dto.setUrlTela("urlTela");
		dto.setDescricaoTela("descricao tela");
		dto.setVersao(1L);
		dto.setXml("xml");
		dto.setXhtml("xhtml");
		dto.setRegistroAuditoria(r);
		
		

		return dto;

	}

	public static enum TipoMetaDadoBuilder {

		INSTANCIA;
	}
}
