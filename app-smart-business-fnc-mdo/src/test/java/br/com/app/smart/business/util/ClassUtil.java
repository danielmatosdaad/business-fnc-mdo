package br.com.app.smart.business.util;

import br.appsmartbusiness.persistencia.conversores.Conversor;
import br.appsmartbusiness.persistencia.dao.facede.AbstractFacade;
import br.appsmartbusiness.persistencia.dao.facede.IFacedeDAO;
import br.appsmartbusiness.persistencia.service.ServiceDAO;

public class ClassUtil {

	public static Class[] getClassInfraDAO() {

		Class[] classeInfraDAO = { AbstractFacade.class, IFacedeDAO.class, Conversor.class, ServiceDAO.class };
		return classeInfraDAO;
	}
}
