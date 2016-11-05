package br.com.app.smart.business.service;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
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
import br.com.app.smart.business.databuilder.PerfilBuilder;

import br.com.app.smart.business.databuilder.PerfilBuilder.TipoPerfilBuilder;
import br.com.app.smart.business.exception.InfraEstruturaException;
import br.com.app.smart.business.exception.NegocioException;
import br.com.app.smart.business.funcionalidade.dto.PerfilDTO;
import br.com.app.smart.business.model.Perfil;
import br.com.app.smart.business.util.ClassUtil;
import br.com.app.smart.business.util.PackageUtil;

/**
 * @author daniel-matos
 *
 */
@RunWith(Arquillian.class)
public class PerfilServiceImpTest {

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

	@EJB(beanName = "PerfilServiceImp", beanInterface = IServicoRemoteDAO.class)
	private IServicoRemoteDAO<PerfilDTO> remote;

	@EJB(beanName = "PerfilServiceImp", beanInterface = IServicoLocalDAO.class)
	private IServicoLocalDAO<PerfilDTO> local;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * 
	 * @throws NegocioException
	 * @throws InfraEstruturaException
	 * @see a) processo define-se em criar sempre persisitir tres registros b)
	 *      buscar os dois registros e verificar se os dois novos ids c) alterar
	 *      os dois registros e verificar se esses registros foram d) excluir
	 *      todos regristros
	 */
	@Test
	public void crud() throws InfraEstruturaException, NegocioException {

		// testando inserao
		PerfilDTO dto = PerfilBuilder.getInstanceDTO(TipoPerfilBuilder.INSTANCIA);
		PerfilDTO dto2 = PerfilBuilder.getInstanceDTO(TipoPerfilBuilder.INSTANCIA);
		dto = this.local.adiconar(dto);
		dto2 = this.local.adiconar(dto2);
		PerfilDTO resutaldoBusca = this.local.bustarPorID(dto.getId());
		Assert.assertNotNull(resutaldoBusca);
		Assert.assertEquals(dto.getId().longValue(), resutaldoBusca.getId().longValue());
		PerfilDTO resutaldoBusca2 = this.local.bustarPorID(dto2.getId());
		Assert.assertNotNull(resutaldoBusca2);
		Assert.assertEquals(dto2.getId().longValue(), resutaldoBusca2.getId().longValue());

		List<PerfilDTO> todos = local.bustarTodos();
		Assert.assertNotNull(todos);
		System.out.println("Buscar todos: " + todos.size());

		int range[] = { 0, 2 };
		List<PerfilDTO> todosIntervalo = local.bustarPorIntervaloID(range);
		Assert.assertNotNull(todosIntervalo);
		System.out.println("bustarPorIntervaloID: " + todosIntervalo.size());
		Assert.assertTrue(todosIntervalo.size() == 2);

		resutaldoBusca2.setDescricao("xml2");

		PerfilDTO resutaldoBusca3 = local.alterar(resutaldoBusca2);
		Assert.assertEquals(resutaldoBusca2.getDescricao(), resutaldoBusca3.getDescricao());

		local.remover(resutaldoBusca3);

		PerfilDTO dto4 = local.bustarPorID(resutaldoBusca3.getId());
		Assert.assertNull(dto4);

	}

	@Test
	public void testeCriarPerfilFuncinalidadeVinculadoso() throws InfraEstruturaException, NegocioException {

		// limpando a base

		System.out.println("sucesso testeCriarPerfilFuncinalidadeVinculadoso");
	}

	//
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

	@Test
	public void testeArvore() throws InfraEstruturaException, NegocioException {

		// exluindo os todos os nos

		List<PerfilDTO> listaRemover = this.local.bustarTodos();
		for (PerfilDTO perfilDTO : listaRemover) {
			System.out.println("Vou remover o id: " + perfilDTO.getId());
			if (perfilDTO.getPerfilPai() != null) {
				System.out.println("Com o id Pai : " + perfilDTO.getPerfilPai().getId());
			}
		}

		System.out.println("-----------------------testeArvore teste construcao---------------------");
		// do raiz null
		// noRaiz
		// / 1 \
		// / \
		// / \
		// / \
		// noSubRaizN1E noSubRaizN1D
		// / 2 \ / 3 \
		// / \ / \
		// / \ / \
		// noSubRaizN2EE noSubRaizN2ED noSubRaizN2DE noSubRaizN2DD
		// 4 5 6 7
		PerfilDTO noZero = PerfilBuilder.getInstanceDTO(TipoPerfilBuilder.INSTANCIA);

		printDTO(noZero);
		noZero = this.local.adiconar(noZero);
		PerfilDTO resutaldoNoZero = this.local.bustarPorID(noZero.getId());

		Assert.assertNotNull(resutaldoNoZero);
		Assert.assertEquals(noZero.getId().longValue(), resutaldoNoZero.getId().longValue());

		System.out.println("noZero id" + resutaldoNoZero.getId());
		Assert.assertNull(resutaldoNoZero.getPerfilPai());

		System.out.println("--------------------------------------------------------------------------------");

		PerfilDTO noUm = PerfilBuilder.getInstanceDTO(TipoPerfilBuilder.INSTANCIA);
		noUm.setPerfilPai(noZero);
		printDTO(noUm);
		noUm = this.local.adiconar(noUm);

		PerfilDTO resutaldoNoUm = this.local.bustarPorID(noUm.getId());

		Assert.assertNotNull(resutaldoNoUm);
		Assert.assertEquals(noUm.getId().longValue(), resutaldoNoUm.getId().longValue());

		System.out.println("resutaldoNoUm id" + resutaldoNoUm.getId());
		if (resutaldoNoUm.getPerfilPai() == null) {

			System.out.println("resutaldoNoUm idPai null");
		} else {

			System.out.println("resutaldoNoUm idPai" + resutaldoNoUm.getPerfilPai().getId());
		}

		System.out.println("--------------------------------------------------------------------------------");

		PerfilDTO noDois = PerfilBuilder.getInstanceDTO(TipoPerfilBuilder.INSTANCIA);

		noDois.setPerfilPai(noZero);
		printDTO(noDois);
		noDois = this.local.adiconar(noDois);

		PerfilDTO resutaldoNoDois = this.local.bustarPorID(noDois.getId());

		Assert.assertNotNull(resutaldoNoDois);
		Assert.assertEquals(noDois.getId().longValue(), resutaldoNoDois.getId().longValue());

		System.out.println("resutaldoNoDois id" + resutaldoNoDois.getId());
		System.out.println("resutaldoNoDois idPai" + resutaldoNoDois.getPerfilPai() == null ? null
				: resutaldoNoDois.getPerfilPai().getId());

		System.out.println("--------------------------------------------------------------------------------");

		PerfilDTO noTres = PerfilBuilder.getInstanceDTO(TipoPerfilBuilder.INSTANCIA);

		noTres.setPerfilPai(noUm);
		printDTO(noTres);
		noTres = this.local.adiconar(noTres);

		PerfilDTO resutaldoNoTres = this.local.bustarPorID(noTres.getId());

		Assert.assertNotNull(resutaldoNoTres);
		Assert.assertEquals(noTres.getId().longValue(), resutaldoNoTres.getId().longValue());

		System.out.println("resutaldoNoTres id" + resutaldoNoTres.getId());
		System.out.println("resutaldoNoTres idPai" + resutaldoNoTres.getPerfilPai() == null ? null
				: resutaldoNoTres.getPerfilPai().getId());

		System.out.println("--------------------------------------------------------------------------------");

		PerfilDTO noQuatro = PerfilBuilder.getInstanceDTO(TipoPerfilBuilder.INSTANCIA);
		noQuatro.setPerfilPai(noUm);
		printDTO(noQuatro);
		noQuatro = this.local.adiconar(noQuatro);

		PerfilDTO resutaldoNoQuatro = this.local.bustarPorID(noQuatro.getId());

		Assert.assertNotNull(resutaldoNoQuatro);
		Assert.assertEquals(noQuatro.getId().longValue(), resutaldoNoQuatro.getId().longValue());

		System.out.println("resutaldoNoQuatro id" + resutaldoNoQuatro.getId());
		System.out.println("resutaldoNoQuatro idPai" + resutaldoNoQuatro.getPerfilPai() == null ? null
				: resutaldoNoQuatro.getPerfilPai().getId());

		System.out.println("--------------------------------------------------------------------------------");

		PerfilDTO noCinco = PerfilBuilder.getInstanceDTO(TipoPerfilBuilder.INSTANCIA);

		noCinco.setPerfilPai(noDois);
		printDTO(noCinco);
		noCinco = this.local.adiconar(noCinco);

		PerfilDTO resutaldoNoCinco = this.local.bustarPorID(noCinco.getId());

		Assert.assertNotNull(resutaldoNoCinco);
		Assert.assertEquals(noCinco.getId().longValue(), resutaldoNoCinco.getId().longValue());

		System.out.println("resutaldoNoCinco id" + resutaldoNoCinco.getId());
		System.out.println("resutaldoNoCinco idPai" + resutaldoNoCinco.getPerfilPai() == null ? null
				: resutaldoNoCinco.getPerfilPai().getId());

		System.out.println("--------------------------------------------------------------------------------");

		PerfilDTO noSeis = PerfilBuilder.getInstanceDTO(TipoPerfilBuilder.INSTANCIA);
		noSeis.setPerfilPai(noDois);
		printDTO(noSeis);
		noSeis = this.local.adiconar(noSeis);

		PerfilDTO resutaldoNoSeis = this.local.bustarPorID(noSeis.getId());

		Assert.assertNotNull(resutaldoNoSeis);
		Assert.assertEquals(noSeis.getId().longValue(), resutaldoNoSeis.getId().longValue());

		System.out.println("resutaldoNoSeis id" + resutaldoNoSeis.getId());
		System.out.println("resutaldoNoSeis idPai" + resutaldoNoSeis.getPerfilPai() == null ? null
				: resutaldoNoSeis.getPerfilPai().getId());

		System.out.println("-----------------------FIM---------------------");
		System.out.println("-----------------------testeArvore - Teste Busca---------------------");

		// do raiz null
		// noRaiz
		// / 1 \
		// / \
		// / \
		// / \
		// noSubRaizN1E noSubRaizN1D
		// / 2 \ / 3 \
		// / \ / \
		// / \ / \
		// noSubRaizN2EE noSubRaizN2ED noSubRaizN2DE noSubRaizN2DD
		// 4 5 6 7

		List<PerfilDTO> resutaldoNoRaizArvore = this.local.bustarTodos();

		for (PerfilDTO perfilDTO : resutaldoNoRaizArvore) {

			if (perfilDTO.getId() == noZero.getId()) {

				List<PerfilDTO> listaFilhoNoRaiz = perfilDTO.getPerfilFilhos();
				Assert.assertNotNull(listaFilhoNoRaiz);
				System.out.println("Filho: listaFilhoNoRaiz.get(0)." + listaFilhoNoRaiz.get(0).getId());
				System.out.println("Filho: listaFilhoNoRaiz.get(1)." + listaFilhoNoRaiz.get(1).getId());
				Assert.assertEquals(perfilDTO.getPerfilFilhos().get(0).getId(), noUm.getId());
				Assert.assertEquals(perfilDTO.getPerfilFilhos().get(1).getId(), noDois.getId());
			}

			if (perfilDTO.getId() == noUm.getId()) {

				List<PerfilDTO> listaFilhoNoRaiz = perfilDTO.getPerfilFilhos();
				Assert.assertNotNull(listaFilhoNoRaiz);
				System.out.println("Filho: noSubRaizN1E.get(0)." + listaFilhoNoRaiz.get(0).getId());
				System.out.println("Filho: noSubRaizN1E.get(1)." + listaFilhoNoRaiz.get(1).getId());
				Assert.assertEquals(listaFilhoNoRaiz.get(0).getId(), noTres.getId());
				Assert.assertEquals(listaFilhoNoRaiz.get(1).getId(), noQuatro.getId());
			}

			if (perfilDTO.getId() == noDois.getId()) {

				List<PerfilDTO> listaFilhoNoRaiz = perfilDTO.getPerfilFilhos();
				Assert.assertNotNull(listaFilhoNoRaiz);

				System.out.println("Filho: noSubRaizN1D.get(0)." + listaFilhoNoRaiz.get(0).getId());
				System.out.println("Filho: noSubRaizN1D.get(1)." + listaFilhoNoRaiz.get(1).getId());
				Assert.assertEquals(listaFilhoNoRaiz.get(0).getId(), noCinco.getId());
				Assert.assertEquals(listaFilhoNoRaiz.get(1).getId(), noSeis.getId());
			}

		}

			listaRemover = this.local.bustarTodos();

			this.local.remover(noSeis);
			this.local.remover(noCinco);
			this.local.remover(noQuatro);
			this.local.remover(noTres);
			this.local.remover(noDois);
			this.local.remover(noUm);
			this.local.remover(noZero);
			
			listaRemover = this.local.bustarTodos();
			
			Assert.assertTrue(listaRemover.size()==0);

	}

	private void printDTO(PerfilDTO noSubRaizN1E) {
		System.out.println("PerfilDTO id: " + noSubRaizN1E.getId());
		if (noSubRaizN1E.getPerfilPai() != null) {

			System.out.println("PerfilDTO idPai : " + noSubRaizN1E.getPerfilPai().getId());
		} else {

			System.out.println("PerfilDTO idPai : null");
		}
	}

}
