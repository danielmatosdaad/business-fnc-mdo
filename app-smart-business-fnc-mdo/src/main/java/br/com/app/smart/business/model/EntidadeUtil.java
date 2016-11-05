package br.com.app.smart.business.model;

public abstract class EntidadeUtil {

	public static String getNomeSequence(final Class clazz){
		
		return "SEQ_"+clazz.getSimpleName().toUpperCase();
	}
}
