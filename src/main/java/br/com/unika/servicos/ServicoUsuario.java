package br.com.unika.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.dao.UsuarioDAO;
import br.com.unika.interfaces.IServico;
import br.com.unika.modelo.Usuario;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;

public class ServicoUsuario implements IServico<Usuario, Long>, Serializable {

	private static final long serialVersionUID = 1L;

	@SpringBean(name = "usuarioDAO")
	private UsuarioDAO usuarioDAO;

	@Override
	public Retorno incluir(Usuario usuario) {

		Retorno retorno = new Retorno(true, null);
		retorno = Validacao.verificarCamposObrigatorios(usuario);
		validacaoDeNegocio(usuario);

		if (!retorno.isSucesso()) {
			return retorno;
		}

		retorno = usuarioDAO.salvarDAO(usuario);

		return retorno;
	}

	@Override
	public Retorno alterar(Usuario usuario) {

		Retorno retorno = new Retorno(true, null);
		retorno = Validacao.verificarCamposObrigatorios(usuario);

		if (!retorno.isSucesso()) {
			return retorno;
		}

		retorno = usuarioDAO.alterarDAO(usuario);

		return retorno;

	}

	@Override
	public Usuario procurar(Usuario usuario) {
		if (usuario != null && usuario.getIdUsuario() != null) {

			return usuarioDAO.procurarDAO(usuario.getIdUsuario());

		}
		return null;
	}

	@Override
	public List<Usuario> listar() {
		return usuarioDAO.listarDAO();

	}

	@Override
	public Retorno remover(Usuario usuario) {

		Retorno retorno = new Retorno(true, null);

		if (usuario != null && usuario.getIdUsuario() != null) {

			retorno = usuarioDAO.removerDAO(usuario);

		} else {
			System.out.println("Nenhuma PK inserida ou com o valor null");
			retorno.setSucesso(false);
			retorno.addMensagem("Nenhuma PK inserida ou com o valor null");
		}
		return retorno;
	}

	@Override
	public List<Usuario> search(Search search) {
		ArrayList<Usuario> lista = new ArrayList<>();
		lista = (ArrayList<Usuario>) usuarioDAO.searchDAO(search);

		return lista;
	}

	private Retorno validacaoDeNegocio(Usuario usuario) {
		Retorno retorno = new Retorno(true, null);

		if (usuario.getNome().length() < 3 || usuario.getNome().length() > 20) {
			retorno.setSucesso(false);
			retorno.addMensagem("Nome Deve Ter Entre 3 e 20 Digitos!");
		}

		if (usuario.getSobrenome().length() < 3 || usuario.getSobrenome().length() > 20) {
			retorno.setSucesso(false);
			retorno.addMensagem("Sobrenome Deve Ter Entre 3 e 20 Digitos!");
		}

		if (usuario.getTelefone().length() < 13 || usuario.getTelefone().length() > 14) {
			retorno.setSucesso(false);
			retorno.addMensagem("Telefone Incorreto!");
		}

		if (usuario.getCpf().length() != 14) {
			retorno.setSucesso(false);
			retorno.addMensagem("CPF Incorreto!");
		} else if (!verificarCPF(usuario.getCpf())) {
			retorno.setSucesso(false);
			retorno.addMensagem("Digito Verificador do CPF Invalido!");
		}
		
		if (!validarEmail(usuario.getEmail())) {
			retorno.setSucesso(false);
			retorno.addMensagem("Email Invalido!");
		}
		
		if (!validarDataNascimento(usuario.getDataNascimento())) {
			
		}

		return retorno;
	}
	
	private boolean validarDataNascimento(Calendar dataNascimento) {

		dataNascimento.get(Calendar.YEAR);
		dataNascimento.get(Calendar.MONTH);
		dataNascimento.get(Calendar.DAY_OF_MONTH);
		
		return false;
	}

	private boolean validarEmail(String email)
    {
       
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

	private boolean verificarCPF(String cpf) {
		cpf = cpf.replaceAll("[^0-9]", "");
		int primeiroDigito = 0;
		int segundoDigito = 0;
		int aux = 0;
		int j = 10;
		
		for (int i = 0; i < cpf.length() - 2; i++) {
			int d = Integer.parseInt(cpf.substring(i, i + 1));
			primeiroDigito = primeiroDigito + (d * j);
			j--;
		}
		
		aux = 11 - primeiroDigito % 11;
		if (aux > 9) {
			primeiroDigito = 0;
		} else {
			primeiroDigito = aux;
		}
		
		j = 11;
		for (int i = 0; i < cpf.length() - 1; i++) {
			int d = Integer.parseInt(cpf.substring(i, i + 1));
			segundoDigito = segundoDigito + (d * j);
			j--;
		}
		
		aux = 11 - segundoDigito % 11;
		if (aux > 9) {
			segundoDigito = 0;
		} else {
			segundoDigito = aux;
		}
		
	
		
		if (cpf.substring(cpf.length()-2,cpf.length()).equals(""+primeiroDigito+segundoDigito)) {
			return true;
		}else {
			return false;
		}
	}

	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}
}
