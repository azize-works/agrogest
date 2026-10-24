package mz.isctem.agrogest.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Transforma passwords em hash (SHA-256 com salt), para nunca as guardar em texto.
 * O resultado tem o formato "salt:hash" (ambos em Base64).
 */
public class Hash {

    private static final SecureRandom ALEATORIO = new SecureRandom();

    /** Gera o hash de uma password, com um salt aleatorio novo. */
    public static String gerar(String password) {
        byte[] salt = new byte[16];
        ALEATORIO.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + calcular(salt, password);
    }

    /** Verifica se a password escrita corresponde ao hash guardado. */
    public static boolean verificar(String password, String guardado) {
        if (guardado == null || !guardado.contains(":")) {
            return false;
        }
        String[] partes = guardado.split(":");
        byte[] salt = Base64.getDecoder().decode(partes[0]);
        return calcular(salt, password).equals(partes[1]);
    }

    /** SHA-256 de (salt + password), em Base64. */
    private static String calcular(byte[] salt, String password) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(salt);
            byte[] resultado = sha.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(resultado);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponivel", e);
        }
    }
}
