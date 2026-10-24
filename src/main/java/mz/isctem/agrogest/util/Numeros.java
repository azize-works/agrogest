package mz.isctem.agrogest.util;

/**
 * Converte o texto dos formularios em numeros.
 * Se o texto nao for um numero, lanca IllegalArgumentException com a mensagem dada (em portugues).
 */
public class Numeros {

    /** Converte texto em inteiro (ex.: um id). */
    public static int lerInt(String texto, String mensagemErro) {
        try {
            return Integer.parseInt(texto == null ? "" : texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(mensagemErro);
        }
    }

    /** Converte texto em numero decimal; aceita virgula ou ponto (ex.: "320,50"). */
    public static double lerDouble(String texto, String mensagemErro) {
        try {
            double valor = Double.parseDouble(texto == null ? "" : texto.trim().replace(',', '.'));
            if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                throw new NumberFormatException();
            }
            return valor;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(mensagemErro);
        }
    }
}

