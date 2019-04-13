package br.com.w2lima.checksumcalculator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author William Wilson Carvalho de Lima (wwlima@gmail.com)
 * @since 09/04/2019
 */
public class Principal {
	private static Logger log = Logger.getGlobal();

	private static final String MD2 = "MD2";
	private static final String MD5 = "MD5";
	private static final String SHA = "SHA";
	private static final String SHA_1 = "SHA-1";
	private static final String SHA_256 = "SHA-256";
	private static final String SHA_384 = "SHA-384";
	private static final String SHA_512 = "SHA-512";
	private static final String BARRA = "*******************************************************************************";
	private static final String DESENVOLVEDOR = "Desenvolvido por: William Wilson Carvalho de Lima (wwlima@gmail.com)";
	
	public static void main(String[] args) {

		validaArgumentos(args);
		String hashResult = "";

		String arquivo = args[0].toUpperCase();
		
		switch (args[1].toUpperCase()) {
		case MD2:
			hashResult = getMD2(arquivo);
			break;
		case MD5:
			hashResult = getMD5(arquivo);
			break;
		case SHA:
			hashResult = getSHA(arquivo);
			break;
		case SHA_1:
			hashResult = getSHA1(arquivo);
			break;
		case SHA_256:
			hashResult = getSHA256(arquivo);
			break;
		case SHA_384:
			hashResult = getSHA384(arquivo);
			break;
		case SHA_512:
			hashResult = getSHA512(arquivo);			
			break;
		default:
			hashResult = "erro";
		}

		if (args.length == 3 && args[2].equalsIgnoreCase("nolog")) {
			log.info(hashResult);
		} else {
			log.info(getMensagem(args[0], args[1], hashResult));
		}

	}

	public static String getMD2(String arquivo) {
		return getHash(arquivo, MD2);
	}

	public static String getMD5(String arquivo) {
		return getHash(arquivo, MD5);
	}

	public static String getSHA(String arquivo) {
		return getHash(arquivo, SHA);
	}

	public static String getSHA1(String arquivo) {
		return getHash(arquivo, SHA_1);
	}

	public static String getSHA256(String arquivo) {
		return getHash(arquivo, SHA_256);
	}

	public static String getSHA384(String arquivo) {
		return getHash(arquivo, SHA_384);
	}

	public static String getSHA512(String arquivo) {
		return getHash(arquivo, SHA_512);
	}

	private static String getHash(String arquivo, String nome) {
		byte[] result = null;
		MessageDigest md;

		try (FileInputStream fis = new FileInputStream(arquivo);) {
			md = MessageDigest.getInstance(nome);

			byte[] dataBytes = new byte[1024];
			int nread = 0;
			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			result = md.digest();
		} catch (NoSuchAlgorithmException e1) {
			log.log(Level.SEVERE, "Algoritimo não encontrado", e1);
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, "Arquivo não encontrado", e);
		} catch (IOException e2) {
			log.log(Level.SEVERE, "Erro de IO", e2);
		}
		return obtemHash(result);
	}

	private static String obtemHash(byte[] mdbytes) {
		if (mdbytes == null)
			return "";

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}

	private static void validaArgumentos(String[] args) {
		if (args.length == 0) {
			log.log(Level.INFO, "{0}", getAviso("São necessários pelo menos 2(dois) argumentos: <arquivo> <hash>"));
			System.exit(-1);
		}

		if (args.length > 3) {
			log.log(Level.INFO, "{0}", getAviso("Apenas dois ou três argumentos são permitidos"));
			System.exit(-1);
		}

		if (args.length < 2) {
			log.log(Level.INFO, "{0}", getAviso("São necessários pelo menos 2(dois) argumentos: <arquivo> <hash>"));
			System.exit(-1);
		}

		File file = new File(args[0]);
		if (!file.exists()) {
			log.log(Level.INFO, "{0}", getAviso("Arquivo não encontrado"));
			System.exit(-1);
		}

		Integer indice = null;
		String[] opcoes = { MD2, MD5, SHA, SHA_1, SHA_256, SHA_384, SHA_512 };
		for (int i = 0; i < opcoes.length; i++) {
			String string = opcoes[i];
			if (string.equalsIgnoreCase(args[1])) {
				indice = i;
				break;
			}
		}

		if (indice == null) {
			log.log(Level.INFO, "{0}", getAviso("Valor inválido para o segundo argumento"));
			System.exit(-1);
		}

	}

	private static String getAviso(String aviso) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n".concat(BARRA));
		sb.append("\n".concat(DESENVOLVEDOR));
		sb.append("\n".concat(BARRA));
		sb.append("\n\n".concat(aviso));
		sb.append("\n\nUso: checksumcalculator <caminho_arquivo> <hash> <nolog>");
		sb.append("\nOpções: ");
		sb.append("\n\nhash: \"MD2\", \"MD5\", \"SHA\", \"SHA-1\", \"SHA-256\", \"SHA-384\", \"SHA-512\"");
		sb.append("\n\nOpcional: \nnolog - retorna apenas o resultado");
		sb.append("\n\nExemplo: ");
		sb.append("\n\nc:\\java -jar Checksumcalculator.jar ChecksumCalculator.jar SHA-256 <enter>");
		sb.append("\n\n".concat(BARRA));
		return sb.toString();
	}

	private static String getMensagem(String arquivo, String hash, String resultado) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n".concat(BARRA));
		sb.append("\n".concat(DESENVOLVEDOR));
		sb.append("\n".concat(BARRA));
		sb.append("\nArquivo..: " + arquivo);
		sb.append("\nHash.....: " + hash);
		sb.append("\nResultado: " + resultado);
		sb.append("\n".concat(BARRA));
		return sb.toString();
	}
}