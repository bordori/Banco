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
import br.com.unika.util.Reflexao;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;

public class ServicoUsuario implements IServico<Usuario, Long>, Serializable {

	private static final long serialVersionUID = 1L;

	@SpringBean(name = "usuarioDAO")
	private UsuarioDAO usuarioDAO;

	@Override
	public Retorno incluir(Usuario usuario) {
		Retorno retorno = new Retorno(true, null);
		usuario = (Usuario) Validacao.retiraEspacoDesnecessarios(usuario);
		retorno = validacaoDeNegocio(usuario);

		if (!retorno.isSucesso()) {
			return retorno;
		}

		retorno = usuarioDAO.salvarDAO(usuario);

		return retorno;
	}

	@Override
	public Retorno alterar(Usuario usuario) {

		Retorno retorno = new Retorno(true, null);
		usuario = (Usuario) Validacao.retiraEspacoDesnecessarios(usuario);
		retorno = validacaoDeNegocio(usuario);

		if (!retorno.isSucesso()) {
			return retorno;
		}
		
		if (usuario.getIdUsuario() != null) {
			retorno = usuarioDAO.alterarDAO(usuario);
		}else {
			retorno.setSucesso(false);
			retorno.addMensagem("Nenhum Id Informado");
		}
		

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
	
	
	@Override
	public int count(Search search) {
		return usuarioDAO.countDAO(search);
	}
	
	
	

	private Retorno validacaoDeNegocio(Usuario usuario) {
		Retorno retorno = new Retorno(true, null);
		
		

		if (usuario.getNome() == null || usuario.getNome().length() < 3 || usuario.getNome().length() > 20) {
			retorno.setSucesso(false);
			retorno.addMensagem("Nome Deve Ter Entre 3 e 20 Digitos!");
		}

		if (usuario.getSobrenome() == null || usuario.getSobrenome().length() < 3 || usuario.getSobrenome().length() > 20) {
			retorno.setSucesso(false);
			retorno.addMensagem("Sobrenome Deve Ter Entre 3 e 20 Digitos!");
		}

		if (usuario.getTelefone() == null || usuario.getTelefone().length() < 13 || usuario.getTelefone().length() > 14 ) {
			retorno.setSucesso(false);
			retorno.addMensagem("Telefone Incorreto!");
		}else if (!verificaSeCampoExiste("telefone", usuario.getTelefone())) {
			retorno.setSucesso(false);
			retorno.addMensagem("O Telefone "+usuario.getTelefone()+" Ja Esta Cadastrado!");
		}

		if (usuario.getCpf() == null || usuario.getCpf().length() != 14) {
			retorno.setSucesso(false);
			retorno.addMensagem("CPF Incorreto!");
		} else if (!Validacao.validarCPF(usuario.getCpf())) {
			retorno.setSucesso(false);
			retorno.addMensagem("Digito Verificador do CPF Invalido!");
		} else if(!verificaSeCampoExiste("cpf", usuario.getCpf())) {
			retorno.setSucesso(false);
			retorno.addMensagem("O CPF "+usuario.getCpf()+" Ja Esta Cadastrado!");
		}
		
		if (usuario.getEmail() == null || !validarEmail(usuario.getEmail())) {
			retorno.setSucesso(false);
			retorno.addMensagem("Email Invalido!");
		}else if(!verificaSeCampoExiste("email", usuario.getEmail())){
			retorno.setSucesso(false);
			retorno.addMensagem("O Email "+usuario.getEmail()+" Ja Esta Cadastrado!");
		}
		
		if (usuario.getCep() == null || usuario.getCep().length() != 9) {
			retorno.setSucesso(false);
			retorno.addMensagem("Cep Invalido!");
		}
		
		if(usuario.getEndereco() == null || usuario.getEndereco().length() < 5 || usuario.getEndereco().length() > 40) {
			retorno.setSucesso(false);
			retorno.addMensagem("Endereço Deve Ter Mais de 5 Digitos!");
		}
		
		if(usuario.getNumero() == null || usuario.getNumero().length() > 6) {
			retorno.setSucesso(false);
			retorno.addMensagem("Numero Deve Ter Menos de 6 Digitos!");
		}
		
		if (usuario.getComplemento() != null) {
			if (usuario.getComplemento().length() < 3 || usuario.getComplemento().length() > 40) {
				retorno.setSucesso(false);
				retorno.addMensagem("Complemento Deve Ter Mais de 3 Digitos!");	
			}
		}
		
		if (usuario.getSexo() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Sexo Não Esta Preechido!");	
		}
		
		if(usuario.getBairro() == null || usuario.getBairro().length() < 4 || usuario.getBairro().length() > 20) {
			retorno.setSucesso(false);
			retorno.addMensagem("Bairro Deve Ter Entre 4 e 20 Digitos!");
		}
		if(usuario.getLogin() == null || usuario.getLogin().length() < 4 || usuario.getLogin().length() > 12) {
			retorno.setSucesso(false);
			retorno.addMensagem("Login Deve Ter Entre 4 e 12 Digitos!");
		}else if(!verificaSeCampoExiste("login",usuario.getLogin())) {
			retorno.setSucesso(false);
			retorno.addMensagem("O Usuario "+usuario.getLogin()+" Ja Esta Cadastrado!");
		}else if (!Validacao.validaSeTemSoLetraENumeros(usuario.getLogin())) {
			retorno.setSucesso(false);
			retorno.addMensagem("Login Não Deve Ter Espaço ou Acentos");
		}
		
		if (usuario.getSenha() == null || usuario.getSenha().length() < 4 || usuario.getSenha().length() > 12) {
			retorno.setSucesso(false);
			retorno.addMensagem("Senha Deve Ter Entre 4 e 12 Digitos!");
		}else if (!Validacao.validaSeTemSoLetraENumeros(usuario.getSenha())) {
			retorno.setSucesso(false);
			retorno.addMensagem("Senha Não Deve Ter Espaço ou Acentos");
		}
		
		if (usuario.getPermissaoDeAcesso() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Permissão Não Esta Preechido!");	
		}
		return retorno;
	}
	
	

	

	private boolean verificaSeCampoExiste(String campo, String pesquisa) {
		Search search = new Search(Usuario.class);
		search.addFilterEqual(campo, pesquisa);
		int count = count(search);
		
		if (count == 0) {
			return true;
		}
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

	

	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}

	
}
