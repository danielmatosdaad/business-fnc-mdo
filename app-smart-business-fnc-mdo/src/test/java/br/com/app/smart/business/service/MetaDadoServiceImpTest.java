package br.com.app.smart.business.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import br.com.app.smart.business.dao.interfaces.IServicoRemoteDAO;
import br.com.app.smart.business.databuilder.MetaDadoBuilder;
import br.com.app.smart.business.databuilder.MetaDadoBuilder.TipoMetaDadoBuilder;
import br.com.app.smart.business.exception.InfraEstruturaException;
import br.com.app.smart.business.exception.NegocioException;
import br.com.app.smart.business.funcionalidade.dto.MetaDadoDTO;
import br.com.app.smart.business.usuario.dto.UsuarioDTO;
import br.com.app.smart.business.util.ClassUtil;
import br.com.app.smart.business.util.PackageUtil;

/**
 * @author daniel-matos
 *
 */
@RunWith(Arquillian.class)
public class MetaDadoServiceImpTest {

	@Deployment
	public static Archive<?> createTestArchive() {

		PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");

		File[] libs = pom.resolve("br.com.app.smart.business:app-smart-business-common:0.0.1-SNAPSHOT")
				.withClassPathResolution(true).withTransitivity().asFile();

		File[] libs2 = pom.resolve("org.modelmapper:modelmapper:0.7.5").withClassPathResolution(true).withTransitivity()
				.asFile();
		
		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
				.addAsLibraries(libs)
				.addAsLibraries(libs2)
				.addClasses(ClassUtil.getClassInfraDAO())
				.addPackage(PackageUtil.DATA_BUILDER.getPackageName())
				.addPackage(PackageUtil.BUILDER_INFRA.getPackageName())
				.addPackage(PackageUtil.CONVERSORES.getPackageName()).addPackage(PackageUtil.ENUMS.getPackageName())
				.addPackage(PackageUtil.EXCEPTION.getPackageName()).addPackage(PackageUtil.MODEL.getPackageName())
				.addPackage(PackageUtil.SERVICE.getPackageName()).addPackage(PackageUtil.UTIL.getPackageName())
				.addPackage(PackageUtil.FACEDE.getPackageName()).addPackage(PackageUtil.DATA.getPackageName())
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsWebInfResource("test-ds.xml", "test-ds.xml");

		System.out.println(war.toString(true));

		return war;
	}

	@EJB(beanName = "MetaDadoServiceImp", beanInterface = IServicoRemoteDAO.class)
	private IServicoRemoteDAO<MetaDadoDTO> metaDadoServiceImp;


	@Inject
	private Logger log;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * 
	 * @see a) processo define-se em criar sempre persisitir tres registros b)
	 *      buscar os dois registros e verificar se os dois novos ids c) alterar
	 *      os dois registros e verificar se esses registros foram d) excluir
	 *      todos regristros
	 */
	@Test
	public void crud() {

		try {

		
			List<MetaDadoDTO> senhasRemover = metaDadoServiceImp.bustarTodos();
			for (MetaDadoDTO MetaDadoDTO : senhasRemover) {
				metaDadoServiceImp.remover(MetaDadoDTO);
			}

			MetaDadoDTO dto = MetaDadoBuilder.getInstanceDTO(TipoMetaDadoBuilder.INSTANCIA);
			MetaDadoDTO dto2 = MetaDadoBuilder.getInstanceDTO(TipoMetaDadoBuilder.INSTANCIA);

			dto = metaDadoServiceImp.adiconar(dto);

			MetaDadoDTO resutaldoBusca = metaDadoServiceImp.bustarPorID(dto.getId());
			Assert.assertNotNull(resutaldoBusca);
			Assert.assertEquals(dto.getId().longValue(), resutaldoBusca.getId().longValue());

			dto2 = metaDadoServiceImp.adiconar(dto2);
			MetaDadoDTO resutaldoBusca2 = metaDadoServiceImp.bustarPorID(dto2.getId());
			Assert.assertNotNull(resutaldoBusca2);

			Assert.assertEquals(dto2.getId().longValue(), resutaldoBusca2.getId().longValue());

			List<MetaDadoDTO> todos = metaDadoServiceImp.bustarTodos();
			Assert.assertNotNull(todos);
			Assert.assertTrue(todos.size() == 2);

			int range[] = { 0, 2 };
			List<MetaDadoDTO> todosIntervalo = metaDadoServiceImp.bustarPorIntervaloID(range);
			Assert.assertNotNull(todosIntervalo);
			Assert.assertTrue(todosIntervalo.size() == 2);

			resutaldoBusca2.setXml("xml2");

			MetaDadoDTO resutaldoBusca3 = metaDadoServiceImp.alterar(resutaldoBusca2);
			Assert.assertEquals(resutaldoBusca2.getXml(), resutaldoBusca3.getXml());

			metaDadoServiceImp.remover(resutaldoBusca3);

			MetaDadoDTO dto4 = metaDadoServiceImp.bustarPorID(resutaldoBusca3.getId());
			Assert.assertNull(dto4);

		} catch (InfraEstruturaException e) {
			e.printStackTrace();
		} catch (NegocioException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void inserirRegistrosIguais() throws InfraEstruturaException, NegocioException {
		MetaDadoDTO dto = MetaDadoBuilder.getInstanceDTO(TipoMetaDadoBuilder.INSTANCIA);
		System.out.println(dto);
		System.out.println(this.metaDadoServiceImp);
		System.out.println(this.metaDadoServiceImp.getClass());
		dto = this.metaDadoServiceImp.adiconar(dto);
		thrown.expect(InfraEstruturaException.class);
		this.metaDadoServiceImp.adiconar(dto);

	}

	@Test
	public void buscarIdNaoExistente() {

		try {
			MetaDadoDTO dto = this.metaDadoServiceImp.bustarPorID(100L);
			Assert.assertNull(dto);

		} catch (InfraEstruturaException e) {
			e.printStackTrace();
		} catch (NegocioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
