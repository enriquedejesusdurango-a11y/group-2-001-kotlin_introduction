package jetbrains.kotlin.course.warmup

fun getGameRules(wordLength: Int, maxAttemptsCount: Int, secretExample: String): String =
    """
    Welcome to the game!

    Two people play this game: one chooses a word (a sequence of letters), the other guesses it. 
    In this version, the computer chooses the word: a sequence of $wordLength letters 
    (for example, $secretExample). The user has several attempts to guess it 
    (the max number is $maxAttemptsCount). For each attempt, the number of complete matches 
    (letter and position) and partial matches (letter only) is reported.

    For example, with ACEB as the hidden word, the BCDF guess will give 
    1 full match (C) and 1 partial match (B).
    """.trimIndent()

fun generateSecret(length: Int = 4): String {
    val letters = ('A'..'Z').toList()
    return letters.shuffled().take(length).joinToString("")
}

fun countExactMatches(secret: String, guess: String): Int =
    secret.zip(guess).count { it.first == it.second }

fun countAllMatches(secret: String, guess: String): Int =
    secret.toSet().sumOf { ch ->
        minOf(secret.count { it == ch }, guess.count { it == ch })
    }

fun countPartialMatches(secret: String, guess: String): Int =
    countAllMatches(secret, guess) - countExactMatches(secret, guess)

fun isWon(secret: String, guess: String): Boolean = secret == guess

fun printRoundResults(secret: String, guess: String) {
    val fullMatches = countExactMatches(secret, guess)
    val partialMatches = countPartialMatches(secret, guess)
    println("Your guess has $fullMatches full matches and $partialMatches partial matches.")
}


fun isLost(isWon: Boolean, attemptIndex: Int, maxAttemptsCount: Int): Boolean =
    !isWon && attemptIndex == maxAttemptsCount

fun playGame(secret: String, maxAttemptsCount: Int) {
    println(getGameRules(secret.length, maxAttemptsCount, "ACEB"))

    var attempt = 0 // se cuenta desde 0
    while (true) {
        println("Please input your guess. It should be of length ${secret.length}.")
        val guess = readLine() ?: ""

        if (guess.length != secret.length) {
            println("Invalid guess length.")
            continue
        }

        printRoundResults(secret, guess)
        val won = isWon(secret, guess)

        if (won) {
            println("Congratulations! You guessed it!")
            return
        }

        
        if (isLost(won, attempt, maxAttemptsCount)) {
            println("Sorry, you lost! :( My word is $secret")
            return
        }

        attempt++
    }
}

fun main() {
    val secret = generateSecret()
    playGame(secret, 3)
}
