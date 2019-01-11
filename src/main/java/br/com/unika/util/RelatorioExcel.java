package br.com.unika.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;

import br.com.unika.modelo.Banco;

public class RelatorioExcel implements Serializable {

	private static final long serialVersionUID = 1L;

	private HSSFSheet sheetTabela;
	private HSSFWorkbook workbook;

	public RelatorioExcel() {
		// TODO Auto-generated constructor stub
	}

	public ByteArrayOutputStream GerarExcelInclusaoAgencia(List<Banco> bancos) {

		workbook = new HSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		sheetTabela = workbook.createSheet("Produtos");

		Row linha0 = sheetTabela.createRow(0);
		
		Cell celula = linha0.createCell(0);
		
		celula.setCellStyle(personalizarCelula());
		celula.setCellValue("Número");
		
		celula.setCellStyle(personalizarCelula());
		celula = linha0.createCell(1);
		celula.setCellValue("Nome");
		
		celula.setCellStyle(personalizarCelula());
		celula = linha0.createCell(2);
		celula.setCellValue("Banco");

		CellRangeAddressList addressList = new CellRangeAddressList(1, 50, 2, 2);
		DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(listaBancos(bancos));
		DataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
		dataValidation.setSuppressDropDownArrow(false);
		
		sheetTabela.addValidationData(dataValidation);
		sheetTabela.autoSizeColumn(0);
		sheetTabela.autoSizeColumn(1);
		sheetTabela.autoSizeColumn(2);
		try {
			/*
			 * FileOutputStream out = new FileOutputStream(new
			 * File(tab.nomeTabela()+".xls"));
			 */
			workbook.write(out);
			System.out.println("Arquivo Excel criado com sucesso!");
			return out;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo não encontrado!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro na edição do arquivo!");
		}
		return null;

	}

	private String[] listaBancos(List<Banco> bancos) {
		List<Banco> lista = bancos;
		String[] retorno = new String[bancos.size()];
		for (int i = 0; i < lista.size(); i++) {
			Banco banco = lista.get(i);
			retorno[i] = banco.getNumeroNomeBanco();
		}
		return retorno;
	}

	private CellStyle personalizarCelula() {
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(personalizarFonte());
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);

		return style;
	}

	private HSSFFont personalizarFonte() {
		HSSFFont fonte = workbook.createFont();

		fonte.setFontHeightInPoints((short) 12);
		fonte.setFontName("Times New Roman"); // fonte.setBoldweight((short) 12);

		return fonte;
	}

	/*private CellStyle personalizarCelulaTitulo(int tamanho) {
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setFont(personalizarFonteTitulo(tamanho));
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);

		return style;
	}

	private HSSFFont personalizarFonteTitulo(int tamanho) {
		HSSFFont fonte = workbook.createFont();

		fonte.setFontHeightInPoints((short) tamanho);
		fonte.setFontName("Times New Roman");
		fonte.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		return fonte;
	}*/

}
