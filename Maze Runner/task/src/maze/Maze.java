package maze;

import java.util.*;

public class Maze {
    private final int wight;
    private final int length;

    private final int[][] maze;

    private static int entry = 0;
    private static int exit;

    public Maze(int wight, int length) {
        this.wight = wight;
        this.length = length;
        this.maze = createMaze(wight, length);
    }

    public Maze(List<String> list) {
        this.wight = list.size() - 1;
        this.length = list.get(0).length() / 2;
        int[][] maze = new int[wight][length];
        int k = 0;
        for (String line :
                list) {
            int j = 0;
            for (int i = 0; i < line.length(); i += 2) {
                if (line.charAt(i) == ' ' && (k == 0 || j == 0 || k == wight - 1 || j == length - 1)) {
                    int key = getKeyByCoordinates(k, j, length);
                    if (entry != 0) {
                        exit = key;
                    } else {
                        entry = key;
                    }
                }
                if (line.charAt(i) == '\u2588') {
                    maze[k][j] = 5;
                } else if (line.charAt(i) == ' ') {
                    maze[k][j] = 0;
                }
                j++;
            }
            k++;
        }
        this.maze = maze;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < wight; i++) {
            for (int j = 0; j < length; j++) {
                if (maze[i][j] == 5) {
                    result.append("\u2588\u2588");
                } else if (maze[i][j] == 3) {
                    result.append("//");
                } else {
                    result.append("  ");
                }

            }
            result.append("\n");
        }
        return result.toString();
    }

    private static int[][] createMaze(int wight, int length) {
        List<Integer> boarderCeilsByKeys = new ArrayList<>();

        Random random = new Random();
        int[][] maze = new int[wight][length];
        for (int i = 0; i < wight; i++) {
            for (int j = 0; j < length; j++) {
                if ((i == 0 || j == 0 || i == wight - 1 || j == length - 1) |
                        (i % 2 == 0 && j % 2 == 0)) {
                    maze[i][j] = 5;
                } else if (i % 2 != 0 && j % 2 != 0) {
                    maze[i][j] = 0;
                } else {
                    maze[i][j] = random.nextInt(2) + 1;
                }
            }
        }

        for (int i = 0; i < wight; i++) {
            for (int j = 0; j < length; j++) {
                if (i == 0 || j == 0 || i == wight - 1 || j == length - 1) {
                    int key = getKeyByCoordinates(i, j, length);
                    boarderCeilsByKeys.add(key);
                }
            }
        }

        Set<Integer> primsEdgesByKeys = new HashSet<>();
        Set<Integer> visitedVertexByKeys = new HashSet<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i < wight && j < length && i >= 0 && j >= 0) {
                    if (i == 1 && j == 1) {
                        int key = getKeyByCoordinates(i, j, length);
                        visitedVertexByKeys.add(key);
                    }
                    if (maze[i][j] == 1 || maze[i][j] == 2) {
                        int key = getKeyByCoordinates(i, j, length);
                        primsEdgesByKeys.add(key);
                    }
                }
            }
        }

        while (!primsEdgesByKeys.isEmpty()) {
            int min = 2;
            for (Integer primsEdge :
                    primsEdgesByKeys) {
                int i = (primsEdge - 1) / length;
                int j = primsEdge - length * i - 1;
                if (maze[i][j] < min) {
                    min = 1;
                    break;
                }
            }
            List<Integer> minimumEdges = new ArrayList<>();
            for (Integer primsEdge :
                    primsEdgesByKeys) {
                int i = (primsEdge - 1) / length;
                int j = primsEdge - length * i - 1;
                if (maze[i][j] == min) {
                    minimumEdges.add(primsEdge);
                }
            }
            int randomNumber = random.nextInt(minimumEdges.size());
            int randomEdge = minimumEdges.get(randomNumber);
            int iOfRandomEdge = (randomEdge - 1) / length;
            int jOfRandomEdge = randomEdge - length * iOfRandomEdge - 1;

            boolean allowed = true;
            int firstNeighbour;
            int secondNeighbour;
            if (iOfRandomEdge % 2 == 0) {
                firstNeighbour = getKeyByCoordinates(iOfRandomEdge - 1, jOfRandomEdge, length);
                secondNeighbour = getKeyByCoordinates(iOfRandomEdge + 1, jOfRandomEdge, length);
            } else {
                firstNeighbour = getKeyByCoordinates(iOfRandomEdge, jOfRandomEdge - 1, length);
                secondNeighbour = getKeyByCoordinates(iOfRandomEdge, jOfRandomEdge + 1, length);
            }
            if (visitedVertexByKeys.contains(firstNeighbour) && visitedVertexByKeys.contains(secondNeighbour)) {
                allowed = false;
            }

            if (allowed) {
                for (int i = iOfRandomEdge - 1; i < iOfRandomEdge + 2; i++) {
                    for (int j = jOfRandomEdge - 1; j < jOfRandomEdge + 2; j++) {
                        if (i % 2 != 0 && j % 2 != 0) {
                            int key = getKeyByCoordinates(i, j, length);
                            visitedVertexByKeys.add(key);
                        }
                    }
                }

                if (iOfRandomEdge % 2 == 0) {
                    for (int i = iOfRandomEdge - 2; i < iOfRandomEdge + 3; i++) {
                        for (int j = jOfRandomEdge - 1; j < jOfRandomEdge + 2; j++) {
                            if (i < wight && j < length && i >= 0 && j >= 0) {
                                if (maze[i][j] == 1 || maze[i][j] == 2) {
                                    int key = getKeyByCoordinates(i, j, length);
                                    primsEdgesByKeys.add(key);
                                }
                            }
                        }
                    }
                } else {
                    for (int i = iOfRandomEdge - 1; i < iOfRandomEdge + 2; i++) {
                        for (int j = jOfRandomEdge - 2; j < jOfRandomEdge + 3; j++) {
                            if (i < wight && j < length && i >= 0 && j >= 0) {
                                if (maze[i][j] == 1 || maze[i][j] == 2) {
                                    int key = getKeyByCoordinates(i, j, length);
                                    primsEdgesByKeys.add(key);
                                }
                            }
                        }
                    }
                }
                maze[iOfRandomEdge][jOfRandomEdge] = 0;
                primsEdgesByKeys.remove(randomEdge);
            } else {
                maze[iOfRandomEdge][jOfRandomEdge] = 5;
                primsEdgesByKeys.remove(randomEdge);
            }

        }

        if (wight >= 3 && length >= 3) {
            entry = 0;
            while (true) {
                int randomNumber = random.nextInt(boarderCeilsByKeys.size());
                int randomBoardCeil = boarderCeilsByKeys.get(randomNumber);
                int iOfRandomBoardCeil = (randomBoardCeil - 1) / length;
                int jOfRandomBoardCeil = randomBoardCeil - length * iOfRandomBoardCeil - 1;

                if ((iOfRandomBoardCeil == 0 && jOfRandomBoardCeil == 0) |
                        (iOfRandomBoardCeil == wight - 1 && jOfRandomBoardCeil == length - 1) |
                        (iOfRandomBoardCeil == 0 && jOfRandomBoardCeil == length - 1) |
                        (iOfRandomBoardCeil == wight - 1 && jOfRandomBoardCeil == 0)) {
                    continue;
                }

                boolean nearbyPass = false;
                int key1 = 0;
                int key2 = 0;
                if (iOfRandomBoardCeil + 1 < wight) {
                    if (jOfRandomBoardCeil + 1 < length) key1 = getKeyByCoordinates(iOfRandomBoardCeil, jOfRandomBoardCeil + 1, length);
                    if (jOfRandomBoardCeil - 1 >= 0) key2 = getKeyByCoordinates(iOfRandomBoardCeil, jOfRandomBoardCeil - 1, length);
                    if (maze[iOfRandomBoardCeil + 1][jOfRandomBoardCeil] == 0 && key1 != entry && key2 != entry) nearbyPass = true;
                }
                if (iOfRandomBoardCeil - 1 >= 0) {
                    if (jOfRandomBoardCeil + 1 < length) key1 = getKeyByCoordinates(iOfRandomBoardCeil, jOfRandomBoardCeil + 1, length);
                    if (jOfRandomBoardCeil - 1 >= 0) key2 = getKeyByCoordinates(iOfRandomBoardCeil, jOfRandomBoardCeil - 1, length);
                    if (maze[iOfRandomBoardCeil - 1][jOfRandomBoardCeil] == 0 && key1 != entry && key2 != entry) nearbyPass = true;
                }
                if (jOfRandomBoardCeil + 1 < length) {
                    if (iOfRandomBoardCeil + 1 < wight) key1 = getKeyByCoordinates(iOfRandomBoardCeil + 1, jOfRandomBoardCeil, length);
                    if (iOfRandomBoardCeil - 1 >= 0) key2 = getKeyByCoordinates(iOfRandomBoardCeil - 1, jOfRandomBoardCeil, length);
                    if (maze[iOfRandomBoardCeil][jOfRandomBoardCeil + 1] == 0 && key1 != entry && key2 != entry) nearbyPass = true;
                }
                if (jOfRandomBoardCeil - 1 >= 0) {
                    if (iOfRandomBoardCeil + 1 < wight) key1 = getKeyByCoordinates(iOfRandomBoardCeil + 1, jOfRandomBoardCeil, length);
                    if (iOfRandomBoardCeil - 1 >= 0) key2 = getKeyByCoordinates(iOfRandomBoardCeil - 1, jOfRandomBoardCeil, length);
                    if (maze[iOfRandomBoardCeil][jOfRandomBoardCeil - 1] == 0 && key1 != entry && key2 != entry) nearbyPass = true;
                }
                if (nearbyPass) {
                    if (entry == 0) {
                        entry = randomBoardCeil;
                        maze[iOfRandomBoardCeil][jOfRandomBoardCeil] = 0;
                        boarderCeilsByKeys.remove(randomNumber);
                    } else {
                        maze[iOfRandomBoardCeil][jOfRandomBoardCeil] = 0;
                        exit = randomBoardCeil;
                        break;
                    }
                }
            }
        }
        return maze;
    }

    private static int getKeyByCoordinates(int i, int j, int length) {
        return i * length + j + 1;
    }

    void findTheEscape() {;
        Random random = new Random();
        Stack<Integer> escapePath = new Stack<>();
        Set<Integer> wrongPath = new HashSet<>();
        escapePath.push(entry);
        int currentCeil;
        while (escapePath.peek() != exit) {
            currentCeil = escapePath.peek();
            int iOfCurrentCeil = (currentCeil - 1) / length;
            int jOfCurrentCeil = currentCeil - length * iOfCurrentCeil - 1;
            List<Integer> neighbors = new ArrayList<>();
            if (iOfCurrentCeil + 1 < wight) {
                if (maze[iOfCurrentCeil + 1][jOfCurrentCeil] == 0) {
                    int key = getKeyByCoordinates(iOfCurrentCeil + 1, jOfCurrentCeil, length);
                    if (!wrongPath.contains(key) && !escapePath.contains(key)) {
                        neighbors.add(key);
                    }
                }
            }
            if (iOfCurrentCeil - 1 >= 0) {
                if (maze[iOfCurrentCeil - 1][jOfCurrentCeil] == 0) {
                    int key = getKeyByCoordinates(iOfCurrentCeil - 1, jOfCurrentCeil, length);
                    if (!wrongPath.contains(key) && !escapePath.contains(key)) {
                        neighbors.add(key);
                    }
                }
            }
            if (jOfCurrentCeil + 1 < length) {
                if (maze[iOfCurrentCeil][jOfCurrentCeil + 1] == 0) {
                    int key = getKeyByCoordinates(iOfCurrentCeil, jOfCurrentCeil + 1, length);
                    if (!wrongPath.contains(key) && !escapePath.contains(key)) {
                        neighbors.add(key);
                    }
                }
            }
            if (jOfCurrentCeil - 1 >= 0) {
                if (maze[iOfCurrentCeil][jOfCurrentCeil - 1] == 0) {
                    int key = getKeyByCoordinates(iOfCurrentCeil, jOfCurrentCeil - 1, length);
                    if (!wrongPath.contains(key) && !escapePath.contains(key)) {
                        neighbors.add(key);
                    }
                }
            }
            if (neighbors.size() == 0) {
                wrongPath.add(escapePath.pop());
            } else {
                int randomNumber = random.nextInt(neighbors.size());
                escapePath.push(neighbors.get(randomNumber));
            }
        }

        for (Integer ceil :
                escapePath) {
            int iOfCeil = (ceil - 1) / length;
            int jOfCeil = ceil - length * iOfCeil - 1;
            maze[iOfCeil][jOfCeil] = 3;
        }
    }
}
