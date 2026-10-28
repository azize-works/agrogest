package mz.isctem.agrogest.repo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Grava e le o DataStore num ficheiro (serializacao Java).
 * A gravacao e segura: escreve primeiro num ficheiro temporario e so depois
 * o troca pelo definitivo, para um corte de energia nao estragar os dados.
 */
public class Persistencia {

    private static final String NOME_FICHEIRO = "agrogest.ser";

    /** Pasta dos dados: dentro do Tomcat (catalina.base) ou da pasta actual. */
    private static Path pasta() {
        String base = System.getProperty("catalina.base", System.getProperty("user.dir"));
        return Paths.get(base, "agrogest-dados");
    }

    private static Path ficheiro() {
        return pasta().resolve(NOME_FICHEIRO);
    }

    /** Grava o DataStore em ficheiro. */
    public static synchronized void guardar(DataStore dados) throws IOException {
        Files.createDirectories(pasta());
        Path temporario = pasta().resolve(NOME_FICHEIRO + ".tmp");

        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(temporario))) {
            out.writeObject(dados);
        }
        // Troca o temporario pelo definitivo
        Files.move(temporario, ficheiro(), StandardCopyOption.REPLACE_EXISTING);
    }

    /** Le o DataStore do ficheiro. Se nao existir (primeira vez), devolve uma base vazia. */
    public static synchronized DataStore carregar() throws IOException, ClassNotFoundException {
        if (!Files.exists(ficheiro())) {
            return new DataStore();
        }
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(ficheiro()))) {
            return (DataStore) in.readObject();
        }
    }

    /** Apaga o ficheiro de dados (usado na opcao "eliminar dados" do admin). */
    public static synchronized void eliminar() throws IOException {
        Files.deleteIfExists(ficheiro());
    }

    // --- Informacao sobre o ficheiro (para a pagina de persistencia do admin) ---

    /** Indica se o ficheiro de dados ja existe no disco. */
    public static boolean existeFicheiro() {
        return Files.exists(ficheiro());
    }

    /** Caminho completo do ficheiro de dados. */
    public static String getCaminho() {
        return ficheiro().toAbsolutePath().toString();
    }

    /** Tamanho do ficheiro em bytes (0 se nao existir ou nao for possivel ler). */
    public static long getTamanho() {
        try {
            return existeFicheiro() ? Files.size(ficheiro()) : 0;
        } catch (IOException e) {
            return 0;
        }
    }

    /** Data/hora da ultima gravacao no formato dd/MM/yyyy HH:mm:ss, ou "—" se nao existir. */
    public static String getUltimaModificacao() {
        try {
            if (!existeFicheiro()) {
                return "—";
            }
            LocalDateTime data = LocalDateTime.ofInstant(
                    Files.getLastModifiedTime(ficheiro()).toInstant(), ZoneId.systemDefault());
            return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        } catch (IOException e) {
            return "—";
        }
    }
}
