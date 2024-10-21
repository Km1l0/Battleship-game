package com.example.hellojavafx.models;

import java.util.Random;
import java.util.*;


public class GameBoard {

    /* Array that will contain the complete solution to the board */
    private int[][] solution;
    /* Array that will contain ONLY the numbers initially drawn on the board and that the player can't change */
    private int[][] initial;
    /* Array that will contain player's numbers */
    private int[][] player;

    public int getSolutionAtPosition(int row, int col) {
        // Verificar que la posición sea válida antes de intentar acceder al array
        if (row >= 0 && row < solution.length && col >= 0 && col < solution[row].length) {
            return solution[row][col];
        }
        return -1; // O algún valor que indique que no es válido
    }

    public GameBoard() {
        solution = new int[][] {
                {1, 2, 3, 4, 5, 6},
                {4, 5, 6, 1, 2, 3},
                {2, 1, 4, 3, 6, 5},
                {3, 6, 5, 2, 1, 4},
                {5, 3, 1, 6, 4, 2},
                {6, 4, 2, 5, 3, 1}
        };

        // 0's will be rendered as empty space and will be editable by player
        initial = new int[][] {
                {0, 2, 3, 4, 5, 6},
                {4, 0, 6, 1, 0, 3},
                {0, 1, 4, 0, 6, 0},
                {3, 6, 0, 2, 0, 4},
                {0, 3, 1, 0, 4, 2},
                {6, 0, 2, 5, 0, 1}
        };

        // player's array is initialized as a 6x6 full of zeroes
        player = new int[6][6];
    }

    // returns the solution array
    public int[][] getSolution() {
        return solution;
    }

    // returns the initial filled-in numbers array
    public int[][] getInitial() {
        return initial;
    }

    // returns the player array
    public int[][] getPlayer() {
        return player;
    }

    public int getValidNumberAtPosition(int row, int col) {
        // Verifica si la celda inicial tiene un número
        if (initial[row][col] != 0) { // Suponiendo que 0 representa una celda vacía
            return initial[row][col]; // Retorna el número inicial si hay uno
        }

        // Crea un conjunto de números del 1 al 6
        Set<Integer> validNumbers = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6));

        // Elimina los números que ya están en la fila
        for (int j = 0; j < solution[row].length; j++) {
            validNumbers.remove(player[row][j]); // Asumiendo que player tiene los números actuales
        }

        // Elimina los números que ya están en la columna
        for (int i = 0; i < solution.length; i++) {
            validNumbers.remove(player[i][col]);
        }

        // Elimina los números que ya están en el subcuadro 2x2
        int boxRowStart = (row / 2) * 2;
        int boxColStart = (col / 3) * 3;
        for (int i = boxRowStart; i < boxRowStart + 2; i++) {
            for (int j = boxColStart; j < boxColStart + 3; j++) {
                validNumbers.remove(player[i][j]);
            }
        }

        // Si hay números válidos, elige uno al azar
        if (!validNumbers.isEmpty()) {
            List<Integer> validList = new ArrayList<>(validNumbers);
            Random random = new Random();
            return validList.get(random.nextInt(validList.size()));
        }

        // Retorna -1 si no hay números válidos (podrías lanzar una excepción o manejarlo como prefieras)
        return -1;
    }

    // modifies a value in the player array
    public void modifyPlayer(int val, int row, int col) {
        // Verifica si el arreglo inicial tiene un cero (tratado como un espacio vacío)
        // en la posición donde queremos colocar un número en el arreglo del jugador
        // de esta manera evitamos intersecciones entre los dos
        if (initial[row][col] == 0) {
            // Solo se permiten valores del 1 al 6 inclusivo
            if (val >= 1 && val <= 6) {
                // Verifica si el movimiento es válido
                if (isValidMove(val, row, col)) {
                    player[row][col] = val;
                } else {
                    // Imprime un mensaje de error si el movimiento no es válido
                    System.out.println("Movimiento no válido en (" + row + ", " + col + ").");
                }
            } else {
                // Imprime un mensaje de error si el valor está fuera de rango
                System.out.println("Value passed to player falls out of range. Must be between 1 and 6.");
            }
        } else {
            // Mensaje de error si se intenta modificar una celda inicial
            System.out.println("Cannot modify initial value at (" + row + ", " + col + ").");
        }
    }

    public boolean checkForSuccessGeneral() {
        // Combine the initial and player arrays
        int[][] combined = new int[6][6];
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                // If there's a valid number in the initial array
                if (initial[row][col] != 0) {
                    combined[row][col] = initial[row][col];
                } else {
                    combined[row][col] = player[row][col];
                }
            }
        }

        // Check rows
        for (int row = 0; row < 6; row++) {
            int sum = 0;
            boolean[] seen = new boolean[7]; // For numbers 1 to 6
            for (int col = 0; col < 6; col++) {
                sum += combined[row][col];
                if (combined[row][col] != 0) {
                    if (seen[combined[row][col]]) return false; // Duplicate found
                    seen[combined[row][col]] = true; // Mark number as seen
                }
            }
            if (sum != 21) return false; // Check if sum is 21
        }

        // Check columns
        for (int col = 0; col < 6; col++) {
            int sum = 0;
            boolean[] seen = new boolean[7];
            for (int row = 0; row < 6; row++) {
                sum += combined[row][col];
                if (combined[row][col] != 0) {
                    if (seen[combined[row][col]]) return false;
                    seen[combined[row][col]] = true;
                }
            }
            if (sum != 21) return false;
        }

        // Check 2x3 blocks
        for (int row_offset = 0; row_offset < 6; row_offset += 2) {
            for (int col_offset = 0; col_offset < 6; col_offset += 3) {
                int sum = 0;
                boolean[] seen = new boolean[7];
                for (int row = 0; row < 2; row++) {
                    for (int col = 0; col < 3; col++) {
                        sum += combined[row + row_offset][col + col_offset];
                        if (combined[row + row_offset][col + col_offset] != 0) {
                            if (seen[combined[row + row_offset][col + col_offset]]) return false;
                            seen[combined[row + row_offset][col + col_offset]] = true;
                        }
                    }
                }
                if (sum != 21) return false;
            }
        }

        // If all checks pass, return true
        return true;
    }

    public boolean isValidMove(int val, int row, int col) {
        // Verifica si el valor está en el rango válido (1 a 6)
        if (val < 1 || val > 6) {
            return false; // Valor no válido
        }

        // Verifica si la celda inicial ya tiene un valor diferente de 0 (que representa un número inicial)
        if (initial[row][col] != 0) {
            return false; // No se puede modificar una celda inicial
        }

        // Verifica la fila
        for (int c = 0; c < 6; c++) {
            // Solo verifica el duplicado si la celda no es la misma
            if (c != col && (player[row][c] == val || initial[row][c] == val)) {
                return false; // Duplicado en la fila
            }
        }

        // Verifica la columna
        for (int r = 0; r < 6; r++) {
            // Solo verifica el duplicado si la celda no es la misma
            if (r != row && (player[r][col] == val || initial[r][col] == val)) {
                return false; // Duplicado en la columna
            }
        }

        // Verifica el bloque 2x3
        int blockRow = (row / 2) * 2; // Encuentra el inicio del bloque de 2 filas
        int blockCol = (col / 3) * 3; // Encuentra el inicio del bloque de 3 columnas
        for (int r = blockRow; r < blockRow + 2; r++) {
            for (int c = blockCol; c < blockCol + 3; c++) {
                // Solo verifica el duplicado si la celda no es la misma
                if (r != row || c != col) {
                    if (player[r][c] == val || initial[r][c] == val) {
                        return false; // Duplicado en el bloque
                    }
                }
            }
        }

        return true; // Movimiento válido
    }
}
