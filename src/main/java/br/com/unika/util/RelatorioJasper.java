package br.com.unika.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import br.com.unika.modelo.Movimentacao;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class RelatorioJasper implements Serializable {

	private static final long serialVersionUID = 1L;

	private static byte[] montarRelatorio(HashMap<String, Object> hash, String nomeRelatorio,JRBeanCollectionDataSource lista) {

		String caminho = "br/com/unika/relatorios/" + nomeRelatorio + ".jasper";
		InputStream fonte;
		try {
			fonte = RelatorioJasper.class.getClassLoader().getResource(caminho).openStream();
			try {
				// JasperPrint print = JasperFillManager.fillReport(fonte, hash,new
				// JRBeanCollectionDataSource(lista));
				return JasperRunManager.runReportToPdf(fonte, hash,lista);

			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static byte[] gerarRelatorioMovimentacoes(HashMap<String, Object> hash, String nomeRelatorio,List<Movimentacao> lista) {
		return montarRelatorio(hash, nomeRelatorio,new JRBeanCollectionDataSource(lista));
	}

	

	public InputStream gerarLogo(String caminho) {
		InputStream fonte = null;
		try {
			fonte = this.getClass().getResource(caminho).openStream();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fonte;
	}

}
