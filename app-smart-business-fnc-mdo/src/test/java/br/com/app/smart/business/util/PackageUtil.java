package br.com.app.smart.business.util;

public enum PackageUtil {

	DATA_BUILDER("br.com.app.smart.business.databuilder"),
	BUILDER_INFRA("br.com.app.smart.business.builder.infra"),
	CONVERSORES("br.com.app.smart.business.conversores"),
	DATA("br.com.app.smart.business.data"),
	ENUMS("br.com.app.smart.business.enums"),
	EXCEPTION("br.com.app.smart.business.exception"),
	MODEL("br.com.app.smart.business.model"),
	SERVICE("br.com.app.smart.business.service"),
	FACEDE("br.com.app.smart.business.dao.facede"),
	UTIL("br.com.app.smart.business.util");
	
	private String packageName;

	private PackageUtil(String packageName) {
		this.packageName = packageName;
	}
	
	public String getPackageName() {
		return packageName;
	}
}
