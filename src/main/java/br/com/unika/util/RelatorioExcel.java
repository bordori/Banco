package br.com.unika.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.markup.html.form.upload.FileUpload;

import br.com.unika.modelo.Agencia;
import br.com.unika.modelo.Banco;

public class RelatorioExcel implements Serializable {

	private static final long serialVersionUID = 1L;

	private HSSFSheet sheetTabela;
	private HSSFWorkbook workbook;

	public RelatorioExcel() {
		// TODO Auto-generated constructor stub
	}

	public byte[] GerarExcelInclusaoAgencia(List<Banco> bancos) {

		workbook = new HSSFWorkbook();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		sheetTabela = workbook.createSheet("Produtos");

		sheetTabela.shiftRows(0, 51, 1);
		sheetTabela.setDefaultColumnStyle(0, personalizarCelula());
		sheetTabela.setDefaultColumnStyle(1, personalizarCelula());
		sheetTabela.setDefaultColumnStyle(2, personalizarCelula());

		Row linha0 = sheetTabela.createRow(0);

		Cell celula = linha0.createCell(0);
		celula.setCellValue("Número");
		celula.setCellStyle(personalizarCelulaTitulo(15));

		celula = linha0.createCell(1);
		celula.setCellValue("Nome");
		celula.setCellStyle(personalizarCelulaTitulo(15));

		celula = linha0.createCell(2);
		celula.setCellValue("Banco");
		celula.setCellStyle(personalizarCelulaTitulo(15));

		CellRangeAddressList addressList = new CellRangeAddressList(1, 50, 2, 2);
		DVConstraint dvConstraint = DVConstraint.createExplicitListConstraint(listaBancos(bancos));
		DataValidation dataValidation = new HSSFDataValidation(addressList, dvConstraint);
		dataValidation.setSuppressDropDownArrow(false);

		sheetTabela.addValidationData(dataValidation);
		sheetTabela.setColumnWidth(0, 12 * 256);
		sheetTabela.setColumnWidth(1, 20 * 256);
		sheetTabela.setColumnWidth(2, 20 * 256);

		sheetTabela.setSelected(false);
		sheetTabela.protectSheet("abrenhosa");

		try {
			/*
			 * FileOutputStream out = new FileOutputStream(new
			 * File(tab.nomeTabela()+".xls"));
			 */
			workbook.write(out);
			System.out.println("Arquivo Excel criado com sucesso!");
			return out.toByteArray();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo não encontrado!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro na edição do arquivo!");
		}
		return null;

	}

	public List<Agencia> lerPlanilha(FileUpload fileUpload, List<Banco> listaBancos, Boolean formatoDeArquivo) {// true=.xls
																												// false=.xlsx
		Workbook workbook;
		Sheet sheet = null;
		List<Agencia> listaAgencia = new ArrayList<>();
		if (formatoDeArquivo == true) {
			try {
				workbook = new HSSFWorkbook(fileUpload.getInputStream());
				sheet = workbook.getSheetAt(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				workbook = new XSSFWorkbook(fileUpload.getInputStream());
				sheet = workbook.getSheetAt(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		Iterator<Row> rowIterator = sheet.iterator();
		int count = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			Iterator<Cell> cellIterator = row.cellIterator();

			Agencia agencia = new Agencia();
			if (count != 0) {
				agencia.setIdAgencia(new Long(count+1));
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					
					switch (cell.getColumnIndex()) {
					case 0:
						agencia.setNumero(retornoValorCelulaString(cell));
						break;
					case 1:
						agencia.setNome(retornoValorCelulaString(cell));
						break;
					case 2:
						agencia.setBanco(getBanco(listaBancos, cell.getStringCellValue()));
						break;

					}

				}
				listaAgencia.add(agencia);
			}

			
			count++;
		}

		return listaAgencia;
	}
	
	private String retornoValorCelulaString(Cell cell) {
		String retorno="";
		
		if (cell.getCellTypeEnum() == CellType.STRING) {
			return cell.getStringCellValue();
		}else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			int i = (int) cell.getNumericCellValue();
			return ""+i;
		}
		
		return retorno;
	}

	private Banco getBanco(List<Banco> listaBancos, String celulaBanco) {
		
		
		String[] bancoSeparado = celulaBanco.split("-");
		for (Banco banco2 : listaBancos) {
			if (bancoSeparado[0].equals(banco2.getNumero()) && bancoSeparado[1].equals(banco2.getNome())) {
				return banco2;
			}
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
		style.setFont(personalizarFonte());
		style.setLocked(false);

		return style;
	}

	private HSSFFont personalizarFonte() {
		HSSFFont fonte = workbook.createFont();

		fonte.setFontHeightInPoints((short) 12);
		fonte.setFontName("Times New Roman"); // fonte.setBoldweight((short) 12);

		return fonte;
	}

	private CellStyle personalizarCelulaTitulo(int tamanho) {
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFont(personalizarFonteTitulo(tamanho));
		style.setLocked(true);

		return style;
	}

	private HSSFFont personalizarFonteTitulo(int tamanho) {
		HSSFFont fonte = workbook.createFont();

		fonte.setFontHeightInPoints((short) tamanho);
		fonte.setFontName("Times New Roman");
		fonte.setBold(true);

		return fonte;
	}

}
