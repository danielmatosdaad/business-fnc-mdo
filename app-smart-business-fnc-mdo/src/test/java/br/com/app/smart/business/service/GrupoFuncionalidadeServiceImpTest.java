package br.com.app.smart.business.service;

import static org.junit.Assert.assertNotNull;

import java.io.File;
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

import br.com.app.smart.business.dao.interfaces.IServicoLocalDAO;
import br.com.app.smart.business.dao.interfaces.IServicoRemoteDAO;
import br.com.app.smart.business.databuilder.GrupoFuncionalidadeBuilder;
import br.com.app.smart.business.databuilder.GrupoFuncionalidadeBuilder.GrupoTipoFuncionalidadeBuilder;
import br.com.app.smart.business.exception.InfraEstruturaException;
import br.com.app.smart.business.exception.NegocioException;
import br.com.app.smart.business.funcionalidade.dto.GrupoFuncionalidadeDTO;
import br.com.app.smart.business.util.ClassUtil;
import br.com.app.smart.business.util.PackageUtil;

/**
 * @author daniel-matos
 *
 */
@RunWith(Arquillian.class)
public class GrupoFuncionalidadeServiceImpTest {

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

	@EJB(beanName="GrupoFuncionalidadeServiceImp",beanInterface=IServicoRemoteDAO.class)
	private IServicoRemoteDAO<GrupoFuncionalidadeDTO> remote;

	@EJB(beanName="GrupoFuncionalidadeServiceImp",beanInterface=IServicoLocalDAO.class)
	private IServicoLocalDAO<GrupoFuncionalidadeDTO> local;
	
	@Inject
	private Logger log;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	
	@Test
	public void testTestServiceLocal() {
		assertNotNull(this.local);
		System.out.println(this.local);
		System.out.println(this.local.getClass());
	}

	// Success
	@Test
	public void testTestServiceRemote() {
		assertNotNull(this.remote);
		System.out.println(this.remote);
		System.out.println(this.remote.getClass());
	}
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
			List<GrupoFuncionalidadeDTO> listaRemover = local.bustarTodos();
			for (GrupoFuncionalidadeDTO item : listaRemover) {
				local.remover(item);
			}

			GrupoFuncionalidadeDTO dto = GrupoFuncionalidadeBuilder.getInstanceDTO(GrupoTipoFuncionalidadeBuilder.INSTANCIA);
			GrupoFuncionalidadeDTO dto2 = GrupoFuncionalidadeBuilder.getInstanceDTO(GrupoTipoFuncionalidadeBuilder.INSTANCIA);
			System.out.println(dto);
			dto = local.adiconar(dto);

			GrupoFuncionalidadeDTO resutaldoBusca = local.bustarPorID(dto.getId());
			Assert.assertNotNull(resutaldoBusca);
			Assert.assertEquals(dto.getId().longValue(), resutaldoBusca.getId().longValue());

			dto2 = local.adiconar(dto2);
			GrupoFuncionalidadeDTO resutaldoBusca2 = local.bustarPorID(dto2.getId());
			Assert.assertNotNull(resutaldoBusca2);

			Assert.assertEquals(dto2.getId().longValue(), resutaldoBusca2.getId().longValue());

			List<GrupoFuncionalidadeDTO> todos = local.bustarTodos();
			Assert.assertNotNull(todos);
			Assert.assertTrue(todos.size() == 2);

			int range[] = { 0, 2 };
			List<GrupoFuncionalidadeDTO> todosIntervalo = local.bustarPorIntervaloID(range);
			Assert.assertNotNull(todosIntervalo);
			Assert.assertTrue(todosIntervalo.size() == 2);

			resutaldoBusca2.setDescricao("xml2");

			GrupoFuncionalidadeDTO resutaldoBusca3 = local.alterar(resutaldoBusca2);
			Assert.assertEquals(resutaldoBusca2.getDescricao(), resutaldoBusca3.getDescricao());

			local.remover(resutaldoBusca3);

			GrupoFuncionalidadeDTO dto4 = local.bustarPorID(resutaldoBusca3.getId());
			Assert.assertNull(dto4);

		} catch (InfraEstruturaException e) {
			e.printStackTrace();
		} catch (NegocioException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void inserirRegistrosIguais() throws InfraEstruturaException, NegocioException {
		GrupoFuncionalidadeDTO dto = GrupoFuncionalidadeBuilder.getInstanceDTO(GrupoTipoFuncionalidadeBuilder.INSTANCIA);
		System.out.println(dto);
		System.out.println(this.local);
		System.out.println(this.local.getClass());
		GrupoFuncionalidadeDTO dto2= this.local.adiconar(dto);
		thrown.expect(InfraEstruturaException.class);
		this.local.adiconar(dto2);

	}

	@Test
	public void buscarIdNaoExistente() {

		try {
			GrupoFuncionalidadeDTO dto = this.local.bustarPorID(100L);
			Assert.assertNull(dto);

		} catch (InfraEstruturaException e) {
			e.printStackTrace();
		} catch (NegocioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
