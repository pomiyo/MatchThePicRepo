package com.example.matchthepic

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.matchthepic.models.BoardSize
import com.example.matchthepic.models.MemoryCard
import com.squareup.picasso.Picasso
import kotlin.math.min

class MemoryBoardAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private val cardClickListener: CardClickListener
) :
    RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {


    companion object {
        private const val MARGIN_SIZE = 10
    }

    interface CardClickListener {
        fun onCardClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryBoardAdapter.ViewHolder {
        val cardWidth = parent.width / boardSize.getWidth() - (2 * MARGIN_SIZE)
        val cardHeight = parent.height / boardSize.getHeight() - (2 * MARGIN_SIZE)
        val cardSideLength = min(cardWidth, cardHeight)

        val view = LayoutInflater.from(context).inflate(R.layout.memo_card, parent, false)
        val params = view.findViewById<CardView>(R.id.cardView).layoutParams as MarginLayoutParams
        params.width = cardSideLength
        params.height = cardSideLength
        params.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemoryBoardAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = boardSize.numCards

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageButton = itemView.findViewById<ImageButton>(R.id.imageButton)

        fun bind(position: Int) {
            if (position < cards.size) {
                val memoryCard = cards[position]

                if (memoryCard.isFaceUp) {
                    if (memoryCard.imageUrl != null) {
                        Picasso.get().load(memoryCard.imageUrl).placeholder(R.drawable.ic_imageloading).into(imageButton)
                    } else {
                        imageButton.setImageResource(memoryCard.identifier)
                    }
                } else {
                    imageButton.setImageResource(R.drawable.ic_unlock)
                }

                imageButton.alpha = if (memoryCard.isMatched) .4f else 1.0f
                val colorStateList = if (memoryCard.isMatched) ContextCompat.getColorStateList(context, R.color.gray) else null
                ViewCompat.setBackgroundTintList(imageButton, colorStateList)
            }
            imageButton.setOnClickListener(View.OnClickListener {
                Log.i(this.javaClass.name , "Clicked position $position")
                cardClickListener.onCardClicked(position)
            })
        }
    }

}
