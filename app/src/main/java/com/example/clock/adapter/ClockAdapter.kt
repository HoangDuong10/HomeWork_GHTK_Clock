package com.example.clock.adapter
import com.example.clock.model.Clock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clock.R
import com.example.clock.databinding.ItemClockBinding

class ClockAdapter : RecyclerView.Adapter<ClockAdapter.ClockViewHolder>() {
    var onClickBtn: ((position : Int,clock : Clock) -> Unit)? = null
    var onClickReset: ((position : Int) -> Unit)? = null
    private var listTime : MutableList<Clock> = mutableListOf()
    inner class ClockViewHolder(private var binding : ItemClockBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(clock : Clock, position: Int){
            binding.tvTime.text = formatTime(clock.startTime)
                binding.btnPlayOrPause.setOnClickListener{
                    onClickBtn?.invoke(position,clock)

                }
                binding.btnReset.setOnClickListener{
                    onClickReset?.invoke(position)

                }

                if(clock.timerJob?.isActive == true){
                    binding.btnPlayOrPause.setImageResource(R.drawable.ic_pause)
                }else{
                    binding.btnPlayOrPause.setImageResource(R.drawable.ic_play)
                }


        }
    }

    fun updateClockAtPosition(position: Int, newClock: Clock) {
        if (position in listTime.indices) {
            listTime[position] = newClock
            notifyItemChanged(position)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockViewHolder {
        val binding = ItemClockBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ClockViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listTime.size
    }

    override fun onBindViewHolder(holder: ClockViewHolder, position: Int) {
        holder.bind(listTime[position],position)
    }
    fun addItem(time : Clock){
        listTime.add(time)
        notifyItemInserted(listTime.lastIndex)
    }

    private fun formatTime(seconds: Long): String {
        val minutes = (seconds / 60).toInt()
        val remainingSeconds = (seconds % 60).toInt()

        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

}

//class ClockDiffCallback(
//    private val oldList: List<Clock>,
//    private val newList: List<Clock>
//) : DiffUtil.Callback() {
//
//    override fun getOldListSize() = oldList.size
//
//    override fun getNewListSize() = newList.size
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return oldList[oldItemPosition].id == newList[newItemPosition].id
//    }
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        return oldList[oldItemPosition] == newList[newItemPosition]
//    }
//}