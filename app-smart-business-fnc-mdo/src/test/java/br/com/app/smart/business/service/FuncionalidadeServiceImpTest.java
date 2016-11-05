package br.com.app.smart.business.service;

import static org.junit.Assert.*;

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
import br.com.app.smart.business.databuilder.FuncionalidadeBuilder;
import br.com.app.smart.business.databuilder.FuncionalidadeBuilder.TipoFuncionalidadeBuilder;
import br.com.app.smart.business.exception.InfraEstruturaException;
import br.com.app.smart.business.exception.NegocioException;
import br.com.app.smart.business.funcionalidade.dto.FuncionalidadeDTO;
import br.com.app.smart.business.funcionalidade.dto.MetaDadoDTO;
import br.com.app.smart.business.util.ClassUtil;
import br.com.app.smart.business.util.PackageUtil;

/**
 * @author daniel-matos
 *
 */
@RunWith(Arquillian.class)
public class FuncionalidadeServiceImpTest {

	@Deployment
	public static Archive<?> createTestArchive() {

		PomEquippedResolveStage pom = Maven.resolver().loadPomFromFile("pom.xml");

		File[] libs = pom.resolve("br.com.app.smart.business:app-smart-business-common:0.0.1-SNAPSHOT")
				.withClassPathResolution(true).withTransitivity().asFile();

		File[] libs2 = pom.resolve("org.modelmapper:modelmapper:0.7.5").withClassPathResolution(true).withTransitivity()
				.asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
				.addClasses(ClassUtil.getClassInfraDAO())
				.addAsLibraries(libs)
				.addAsLibraries(libs2)
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

	@EJB(beanName = "FuncionalidadeServiceImp", beanInterface = IServicoRemoteDAO.class)
	private IServicoRemoteDAO<FuncionalidadeDTO> remote;

	@EJB(beanName = "FuncionalidadeServiceImp", beanInterface = IServicoLocalDAO.class)
	private IServicoLocalDAO<FuncionalidadeDTO> local;

	@EJB(beanName = "MetaDadoServiceImp", beanInterface = IServicoLocalDAO.class)
	private IServicoLocalDAO<MetaDadoDTO> localMetaDadoServiceImp;

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

		// limpando a base
		List<FuncionalidadeDTO> listaRemover = this.local.bustarTodos();
		for (FuncionalidadeDTO item : listaRemover) {
			this.local.remover(item);
		}

		// testando inserao
		FuncionalidadeDTO dto = FuncionalidadeBuilder.getInstanceDTO(TipoFuncionalidadeBuilder.INSTANCIA);
		FuncionalidadeDTO dto2 = FuncionalidadeBuilder.getInstanceDTO(TipoFuncionalidadeBuilder.INSTANCIA);
		dto = this.local.adiconar(dto);
		dto2 = this.local.adiconar(dto2);
		FuncionalidadeDTO resutaldoBusca = this.local.bustarPorID(dto.getId());
		Assert.assertNotNull(resutaldoBusca);
		Assert.assertEquals(dto.getId().longValue(), resutaldoBusca.getId().longValue());
		FuncionalidadeDTO resutaldoBusca2 = this.local.bustarPorID(dto2.getId());
		Assert.assertNotNull(resutaldoBusca2);
		Assert.assertEquals(dto2.getId().longValue(), resutaldoBusca2.getId().longValue());

		List<FuncionalidadeDTO> todos = local.bustarTodos();
		Assert.assertNotNull(todos);
		System.out.println("Buscar todos: " + todos.size());
		Assert.assertTrue(todos.size() == 2);

		int range[] = { 0, 2 };
		List<FuncionalidadeDTO> todosIntervalo = local.bustarPorIntervaloID(range);
		Assert.assertNotNull(todosIntervalo);
		System.out.println("bustarPorIntervaloID: " + todosIntervalo.size());
		Assert.assertTrue(todosIntervalo.size() == 2);

		resutaldoBusca2.setDescricao("xml2");

		FuncionalidadeDTO resutaldoBusca3 = local.alterar(resutaldoBusca2);
		Assert.assertEquals(resutaldoBusca2.getDescricao(), resutaldoBusca3.getDescricao());

		local.remover(resutaldoBusca3);

		FuncionalidadeDTO dto4 = local.bustarPorID(resutaldoBusca3.getId());
		Assert.assertNull(dto4);

	}

	@Test
	public void testeCriarFuncinalidadeVinculadaMetaDado() throws InfraEstruturaException, NegocioException {

		// limpando a base
		List<FuncionalidadeDTO> listaRemover = this.local.bustarTodos();
		for (FuncionalidadeDTO item : listaRemover) {
			this.local.remover(item);
		}

		List<MetaDadoDTO> listaMetaDadoDTO = this.localMetaDadoServiceImp.bustarTodos();
		for (MetaDadoDTO item : listaMetaDadoDTO) {
			this.localMetaDadoServiceImp.remover(item);
		}

		FuncionalidadeDTO dto = FuncionalidadeBuilder.getInstanceDTO(TipoFuncionalidadeBuilder.FUNCIONALIDADE_METADADO);

		this.localMetaDadoServiceImp.adiconar(dto.getMetadados());

		MetaDadoDTO resutaldoBusca = this.localMetaDadoServiceImp.bustarPorID(dto.getMetadados().get(0).getId());
		Assert.assertNotNull(resutaldoBusca);
		Assert.assertEquals(dto.getMetadados().get(0).getId().longValue(), resutaldoBusca.getId().longValue());
		// testando inserao
		this.local.adiconar(dto);
		FuncionalidadeDTO resutaldoBuscaFuncionalidade = this.local.bustarPorID(dto.getId());
		Assert.assertNotNull(resutaldoBuscaFuncionalidade);
		Assert.assertEquals(dto.getId().longValue(), resutaldoBuscaFuncionalidade.getId().longValue());
		
		System.out.println("sucesso testeCriarFuncinalidadeVinculadaMetaDado");
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
	public void testeArvoreFuncionalidade() throws InfraEstruturaException, NegocioException{
		
		// exluindo os todos os nos
		System.out.println("-----------------------testeArvore teste construcao---------------------");
				List<FuncionalidadeDTO> listaRemover = this.local.bustarTodos();
				for (FuncionalidadeDTO funcionalidadedto : listaRemover) {
					System.out.println("Vou remover o id: " + funcionalidadedto.getId());
					if (funcionalidadedto.getFuncionalidadePai() != null) {
						System.out.println("Com o id Pai : " + funcionalidadedto.getFuncionalidadePai().getId());
					}
					this.local.remover(funcionalidadedto);
				}

			
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
				FuncionalidadeDTO noZero = FuncionalidadeBuilder.getInstanceDTO(TipoFuncionalidadeBuilder.INSTANCIA);

				printDTO(noZero);
				noZero = this.local.adiconar(noZero);
				FuncionalidadeDTO resutaldoNoZero = this.local.bustarPorID(noZero.getId());

				Assert.assertNotNull(resutaldoNoZero);
				Assert.assertEquals(noZero.getId().longValue(), resutaldoNoZero.getId().longValue());

				System.out.println("noZero id" + resutaldoNoZero.getId());
				Assert.assertNull(resutaldoNoZero.getFuncionalidadePai());

				System.out.println("--------------------------------------------------------------------------------");

				FuncionalidadeDTO noUm = FuncionalidadeBuilder.getInstanceDTO(TipoFuncionalidadeBuilder.INSTANCIA);
				noUm.setFuncionalidadePai(noZero);
				printDTO(noUm);
				noUm = this.local.adiconar(noUm);

				FuncionalidadeDTO resutaldoNoUm = this.local.bustarPorID(noUm.getId());

				Assert.assertNotNull(resutaldoNoUm);
				Assert.assertEquals(noUm.getId().longValue(), resutaldoNoUm.getId().longValue());

				System.out.println("resutaldoNoUm id" + resutaldoNoUm.getId());
				if (resutaldoNoUm.getFuncionalidadePai() == null) {

					System.out.println("resutaldoNoUm idPai null");
				} else {

					System.out.println("resutaldoNoUm idPai" + resutaldoNoUm.getFuncionalidadePai().getId());
				}

				System.out.println("--------------------------------------------------------------------------------");

				FuncionalidadeDTO noDois = FuncionalidadeBuilder.getInstanceDTO(TipoFuncionalidadeBuilder.INSTANCIA);

				noDois.setFuncionalidadePai(noZero);
				printDTO(noDois);
				noDois = this.local.adiconar(noDois);

				FuncionalidadeDTO resutaldoNoDois = this.local.bustarPorID(noDois.getId());

				Assert.assertNotNull(resutaldoNoDois);
				Assert.assertEquals(noDois.getId().longValue(), resutaldoNoDois.getId().longValue());

				System.out.println("resutaldoNoDois id" + resutaldoNoDois.getId());
				System.out.println("resutaldoNoDois idPai" + resutaldoNoDois.getFuncionalidadePai() == null ? null
						: resutaldoNoDois.getFuncionalidadePai().getId());

				System.out.println("--------------------------------------------------------------------------------");

				FuncionalidadeDTO noTres = FuncionalidadeBuilder.getInstanceDTO(TipoFuncionalidadeBuilder.INSTANCIA);

				noTres.setFuncionalidadePai(noUm);
				printDTO(noTres);
				noTres = this.local.adiconar(noTres);

				FuncionalidadeDTO resutaldoNoTres = this.local.bustarPorID(noTres.getId());

				Assert.assertNotNull(resutaldoNoTres);
				Assert.assertEquals(noTres.getId().longValue(), resutaldoNoTres.getId().longValue());

				System.out.println("resutaldoNoTres id" + resutaldoNoTres.getId());
				System.out.println("resutaldoNoTres idPai" + resutaldoNoTres.getFuncionalidadePai() == null ? null
						: resutaldoNoTres.getFuncionalidadePai().getId());

				System.out.println("--------------------------------------------------------------------------------");

				FuncionalidadeDTO noQuatro = FuncionalidadeBuilder.getInstanceDTO(TipoFuncionalidadeBuilder.INSTANCIA);
				noQuatro.setFuncionalidadePai(noUm);
				printDTO(noQuatro);
				noQuatro = this.local.adiconar(noQuatro);

				FuncionalidadeDTO resutaldoNoQuatro = this.local.bustarPorID(noQuatro.getId());

				Assert.assertNotNull(resutaldoNoQuatro);
				Assert.assertEquals(noQuatro.getId().longValue(), resutaldoNoQuatro.getId().longValue());

				System.out.println("resutaldoNoQuatro id" + resutaldoNoQuatro.getId());
				System.out.println("resutaldoNoQuatro idPai" + resutaldoNoQuatro.getFuncionalidadePai() == null ? null
						: resutaldoNoQuatro.getFuncionalidadePai().getId());

				System.out.println("--------------------------------------------------------------------------------");

				FuncionalidadeDTO noCinco = FuncionalidadeBuilder.getInstanceDTO(TipoFuncionalidadeBuilder.INSTANCIA);

				noCinco.setFuncionalidadePai(noDois);
				printDTO(noCinco);
				noCinco = this.local.adiconar(noCinco);

				FuncionalidadeDTO resutaldoNoCinco = this.local.bustarPorID(noCinco.getId());

				Assert.assertNotNull(resutaldoNoCinco);
				Assert.assertEquals(noCinco.getId().longValue(), resutaldoNoCinco.getId().longValue());

				System.out.println("resutaldoNoCinco id" + resutaldoNoCinco.getId());
				System.out.println("resutaldoNoCinco idPai" + resutaldoNoCinco.getFuncionalidadePai() == null ? null
						: resutaldoNoCinco.getFuncionalidadePai().getId());

				System.out.println("--------------------------------------------------------------------------------");

				FuncionalidadeDTO noSeis = FuncionalidadeBuilder.getInstanceDTO(TipoFuncionalidadeBuilder.INSTANCIA);
				noSeis.setFuncionalidadePai(noDois);
				printDTO(noSeis);
				noSeis = this.local.adiconar(noSeis);

				FuncionalidadeDTO resutaldoNoSeis = this.local.bustarPorID(noSeis.getId());

				Assert.assertNotNull(resutaldoNoSeis);
				Assert.assertEquals(noSeis.getId().longValue(), resutaldoNoSeis.getId().longValue());

				System.out.println("resutaldoNoSeis id" + resutaldoNoSeis.getId());
				System.out.println("resutaldoNoSeis idPai" + resutaldoNoSeis.getFuncionalidadePai() == null ? null
						: resutaldoNoSeis.getFuncionalidadePai().getId());

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

				List<FuncionalidadeDTO> resutaldoNoRaizArvore = this.local.bustarTodos();

				for (FuncionalidadeDTO funcionalidadedto : resutaldoNoRaizArvore) {

					if (funcionalidadedto.getId() == noZero.getId()) {

						List<FuncionalidadeDTO> listaFilhoNoRaiz = funcionalidadedto.getFuncionalidadeFilhos();
						Assert.assertNotNull(listaFilhoNoRaiz);
						System.out.println("Filho: listaFilhoNoRaiz.get(0)." + listaFilhoNoRaiz.get(0).getId());
						System.out.println("Filho: listaFilhoNoRaiz.get(1)." + listaFilhoNoRaiz.get(1).getId());
						Assert.assertEquals(funcionalidadedto.getFuncionalidadeFilhos().get(0).getId(), noUm.getId());
						Assert.assertEquals(funcionalidadedto.getFuncionalidadeFilhos().get(1).getId(), noDois.getId());
					}

					if (funcionalidadedto.getId() == noUm.getId()) {

						List<FuncionalidadeDTO> listaFilhoNoRaiz = funcionalidadedto.getFuncionalidadeFilhos();
						Assert.assertNotNull(listaFilhoNoRaiz);
						System.out.println("Filho: noSubRaizN1E.get(0)." + listaFilhoNoRaiz.get(0).getId());
						System.out.println("Filho: noSubRaizN1E.get(1)." + listaFilhoNoRaiz.get(1).getId());
						Assert.assertEquals(listaFilhoNoRaiz.get(0).getId(), noTres.getId());
						Assert.assertEquals(listaFilhoNoRaiz.get(1).getId(), noQuatro.getId());
					}

					if (funcionalidadedto.getId() == noDois.getId()) {

						List<FuncionalidadeDTO> listaFilhoNoRaiz = funcionalidadedto.getFuncionalidadeFilhos();
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
					System.out.println("Tamanho da lista para remover: " + listaRemover.size());
					Assert.assertTrue(listaRemover.size()==0);

		
		
		
	}

	private void printDTO(FuncionalidadeDTO dto) {
	
		System.out.println("FuncionalidadeDTO id: " + dto.getId());
		if (dto.getFuncionalidadePai() != null) {

			System.out.println("Funcionalidade idPai : " + dto.getFuncionalidadePai().getId());
		} else {

			System.out.println("Funcionalidade idPai : null");
		}
		
	}

}
