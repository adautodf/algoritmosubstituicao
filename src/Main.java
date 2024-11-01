import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;


public class Main {
    public static void main(String[] args) {

        int[] sequenciaPaginas = {1, 2, 3, 4, 1, 2, 5, 1, 2, 3, 4, 5};

        System.out.println("Método FIFO - " + metodoFIFO(sequenciaPaginas) + " faltas de página");
        System.out.println("Método LRU - " + metodoLRU(sequenciaPaginas) + " faltas de página");
        System.out.println("Método NFU - " + metodoNFU(sequenciaPaginas) + " faltas de página");
        System.out.println("Método Aging - " + metodoAging(sequenciaPaginas) + " faltas de página");

    }

    private static int metodoFIFO(int[] sequenciaPaginas) {
        Queue<Integer> memoria = new LinkedList<>(); // cria uma fila simulando a memoria
        int faltasPagina = 0;
        for (int pagina : sequenciaPaginas) {
            if (!memoria.contains(pagina)) { // verifica se a página atual não está presente na memória. Se não estiver, significa que houve uma falta de página.
                if (memoria.size() == 4) { //  verifica se a memória está cheia
                    memoria.poll(); // se estiver cheia, remove a página mais antiga
                }
                memoria.offer(pagina); // adiciona a nova página  no final da fila
                faltasPagina++;
            }
        }
        return faltasPagina;
    }

    private static int metodoLRU(int[] sequenciaPaginas) {
        LinkedList<Integer> memoria = new LinkedList<>();
        int faltasPagina = 0;

        for (int pagina : sequenciaPaginas) {
            if (memoria.contains(pagina)) {
                memoria.remove((Integer) pagina); // Remove a página se já estiver na memória
            } else {
                if (memoria.size() == 4) {
                    memoria.poll(); // Remove a página mais antiga da memória (a menos recentemente usada)
                }
                faltasPagina++; // Incrementa o contador de faltas de página
            }
            memoria.offer(pagina); // Adiciona a página mais recentemente usada no final da lista
        }

        return faltasPagina;
    }

    private static int metodoNFU(int[] sequenciaPaginas) {
        Map<Integer, Integer> memoria = new HashMap<>();
        int faltasPagina = 0;

        for (int pagina : sequenciaPaginas) {
            if (memoria.containsKey(pagina)) {
                memoria.put(pagina, memoria.get(pagina) + 1); // Incrementa o contador de referências da página
            } else {
                if (memoria.size() == 4) {
                    int paginaMenosUsada = encontrarPaginaMenosUsada(memoria);
                    memoria.remove(paginaMenosUsada); // Remove a página menos usada da memória
                }
                memoria.put(pagina, 1); // Adiciona a nova página com contador inicializado como 1
                faltasPagina++; // Incrementa o contador de faltas de página
            }
        }

        return faltasPagina;
    }

    private static int encontrarPaginaMenosUsada(Map<Integer, Integer> memoria) {
        int menorContador = Integer.MAX_VALUE;
        int paginaMenosUsada = -1;

        for (Map.Entry<Integer, Integer> entry : memoria.entrySet()) {
            if (entry.getValue() < menorContador) {
                menorContador = entry.getValue();
                paginaMenosUsada = entry.getKey();
            }
        }

        return paginaMenosUsada;
    }

    private static int metodoAging(int[] sequenciaPaginas) {
        LinkedList<Integer> memoria = new LinkedList<>();
        LinkedList<Integer> bitsEnvelhecimento = new LinkedList<>();
        int faltasPagina = 0;

        for (int pagina : sequenciaPaginas) {
            if (memoria.contains(pagina)) {
                int indice = memoria.indexOf(pagina);
                bitsEnvelhecimento.set(indice, bitsEnvelhecimento.get(indice) | 0b10000000); // Define o bit de envelhecimento como 1
            } else {
                if (memoria.size() == 4) {
                    int paginaSubstituida = substituirPagina(memoria, bitsEnvelhecimento);
                    int indice = memoria.indexOf(paginaSubstituida);
                    memoria.set(indice, pagina); // Substitui a página menos recentemente usada
                    bitsEnvelhecimento.set(indice, 0); // Zera o bit de envelhecimento da página substituída
                } else {
                    memoria.add(pagina); // Adiciona a página na memória
                    bitsEnvelhecimento.add(0); // Inicializa o bit de envelhecimento como 0
                }
                faltasPagina++; // Incrementa o contador de faltas de página
            }
            atualizarBitsEnvelhecimento(bitsEnvelhecimento); // Atualiza os bits de envelhecimento de todas as páginas
        }

        return faltasPagina;
    }

    private static int substituirPagina(LinkedList<Integer> memoria, LinkedList<Integer> bitsEnvelhecimento) {
        int indiceSubstituir = 0;
        int menorBit = 0b10000000;

        for (int i = 0; i < memoria.size(); i++) {
            if (bitsEnvelhecimento.get(i) < menorBit) {
                menorBit = bitsEnvelhecimento.get(i);
                indiceSubstituir = i;
            }
        }

        return memoria.get(indiceSubstituir);
    }

    private static void atualizarBitsEnvelhecimento(LinkedList<Integer> bitsEnvelhecimento) {
        for (int i = 0; i < bitsEnvelhecimento.size(); i++) {
            bitsEnvelhecimento.set(i, bitsEnvelhecimento.get(i) >>> 1); // Desloca os bits para a direita
        }
    }

}