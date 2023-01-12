package com.example.matchthepic.models

import com.example.matchthepic.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize, customImages: List<String>?) {

    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var numCardFlips = 0
    private var indexOfSingleSelectedCard: Int? = null

    init {
        if (customImages == null) {
            val chosenImages = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomImages = (chosenImages + chosenImages).shuffled()
            cards = randomImages.map { MemoryCard(it) }
        } else {
            val randomizedImages = (customImages + customImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it.hashCode(), it) }
        }
    }

    fun flipCard(position: Int): Boolean {
       if (position < cards.size) {
           numCardFlips++
           val card = cards[position]
           var foundMatch = false

           if (indexOfSingleSelectedCard == null) {
               restoreCards()
               indexOfSingleSelectedCard = position
           } else {
               foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
               indexOfSingleSelectedCard = null
           }
           card.isFaceUp = !card.isFaceUp
           return foundMatch
       }
        return false
    }

    private fun checkForMatch(pos1: Int, pos2: Int): Boolean {
        if (cards[pos1].identifier != cards[pos2].identifier) {
            return false
        }
        cards[pos1].isMatched = true
        cards[pos2].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardFlips / 2
    }
}