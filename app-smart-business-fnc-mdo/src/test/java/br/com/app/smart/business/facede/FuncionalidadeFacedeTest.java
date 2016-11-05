package br.com.app.smart.business.facede;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Test;
import org.junit.runner.RunWith;
import br.com.app.smart.business.dao.facede.FuncionalidadeFacade;
import br.com.app.smart.business.dao.facede.GrupoFuncionalidadeFacade;
import br.com.app.smart.business.util.PackageUtil;

@RunWith(Arquillian.class)
public class FuncionalidadeFacedeTest {

	@Deployment
	public static Archive<?> createTestArchive() {



		PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");

		File[] libs = pom.resolve("br.com.app.smart.business:app-smart-business-common:0.0.1-SNAPSHOT")
				.withClassPathResolution(true).withTransitivity().asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war").addAsLibraries(libs)
				.addPackage(PackageUtil.DATA_BUILDER.getPackageName())
				.addPackage(PackageUtil.BUILDER_INFRA.getPackageName())
				.addPackage(PackageUtil.CONVERSORES.getPackageName()).addPackage(PackageUtil.ENUMS.getPackageName())
				.addPackage(PackageUtil.EXCEPTION.getPackageName()).addPackage(PackageUtil.MODEL.getPackageName())
				.addPackage(PackageUtil.SERVICE.getPackageName()).addPackage(PackageUtil.UTIL.getPackageName())
				.addPackage(PackageUtil.FACEDE.getPackageName()).addPackage(PackageUtil.DATA.getPackageName())
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("test-ds.xml", "test-ds.xml");

		return war;
	}

	@EJB
	private GrupoFuncionalidadeFacade grupoFuncionalidadeFacade;

	@EJB
	private FuncionalidadeFacade funcionalidadeFacede;
	
	@Test
	public void testInjectionEJB() {
		assertNotNull(this.grupoFuncionalidadeFacade);
		assertNotNull(this.funcionalidadeFacede);
	}

}
