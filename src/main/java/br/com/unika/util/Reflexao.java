package br.com.unika.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.com.unika.modelo.Usuario;

public abstract class Reflexao {
	
	public static Field[] getCampos(Object entidade) {
		if (entidade != null) {
			Field[] campos = entidade.getClass().getDeclaredFields();
			return campos;
		}
		return null;
	}
	
	public static Object getValorDoObjeto(Field campo,Object entidade) {
		String metaDado = Reflexao.obterGetSetDoCampo(campo,"get");
		Object retorno = Reflexao.invocarMetodo(entidade,metaDado);
		return retorno;
	}

	private static Object invocarMetodo(Object entidade, String metaDado) {
		Method meth = null;
		Object retorno = null;
		
		meth = obterMetodo(entidade.getClass(), metaDado);
		
		try {
			retorno = meth.invoke(entidade);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retorno;
	}
	private static Method obterMetodo(Class<?> cls, String nome) {
		Method meth = null;
		try {
			meth = cls.getMethod(nome);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
		return meth;
		
	}

	private static String obterGetSetDoCampo(Field campo,String tipo) {
		String retorno =tipo+ Reflexao.capitularizar(campo );
		return retorno;
	}

	private static String capitularizar(Field campo) {
		String retorno = campo.getName();
		retorno = retorno.substring(0, 1).toUpperCase()+""+retorno.substring(1,retorno.length());
		return retorno;
	}
	
	/*public static void main(String[] args) {
		
		Usuario u = new Usuario();
		
		Field[] c = Reflexao.getCampos(u);
		for (int i = ; i < c.length; i++) {
			Field field = c[i];
			Object r = Reflexao.getValorDoObjeto(field, u);
			System.out.println(r);
			
		}
	}*/

}
