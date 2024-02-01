package games.gameOfFifteen

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    /*
     * Generate a random permutation from 1 to 15.
     * `shuffled()` function might be helpful.
     * If the permutation is not even, make it even (for instance,
     * by swapping two numbers).
     */
    override val initialPermutation by lazy {
         generateRandomEvenPermutation()
    }
    private fun generateRandomEvenPermutation(): List<Int> {
        val permutation = (1..15).shuffled().toList()

        if (!isEven(permutation)) {
            makeEven(permutation as MutableList<Int>)
        }

        return permutation
    }

    private fun isEven(permutation: List<Int>): Boolean {
        var inversionCount = 0

        for (i in permutation.indices) {
            for (j in i + 1 until permutation.size) {
                if (permutation[i] > permutation[j]) {
                    inversionCount++
                }
            }
        }

        return inversionCount % 2 == 0
    }

    private fun makeEven(permutation: MutableList<Int>) {
        // Find two indices to swap
        var index1 = 0
        var index2 = 0

        while (index1 == index2 || permutation[index1] % 2 == permutation[index2] % 2) {
            index1 = (0 until permutation.size).random()
            index2 = (0 until permutation.size).random()
        }

        // Swap the values at the chosen indices
        val temp = permutation[index1]
        permutation[index1] = permutation[index2]
        permutation[index2] = temp
    }
}

