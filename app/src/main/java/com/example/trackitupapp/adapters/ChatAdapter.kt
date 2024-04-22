import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trackitupapp.R
import com.example.trackitupapp.apiServices.responses.ChatMessageResponse
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages: MutableList<ChatMessageResponse> = mutableListOf()
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var lastMessageTime: Long = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ME -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_chat_current_user, parent, false)
                CurUsrMessageViewHolder(view)
            }
            VIEW_TYPE_OTHER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_chat_other_user, parent, false)
                OthrUsrMessageViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder.itemViewType) {
            VIEW_TYPE_ME -> {
                (holder as CurUsrMessageViewHolder).bind(message)
            }
            VIEW_TYPE_OTHER -> {
                (holder as OthrUsrMessageViewHolder).bind(message)
            }
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSent) VIEW_TYPE_ME else VIEW_TYPE_OTHER
    }

    fun addMessage(message: ChatMessageResponse) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    inner class CurUsrMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(chatMessage: ChatMessageResponse) {
            itemView.findViewById<TextView>(R.id.tv_chat_messageCurUsr).text = chatMessage.message
            val currentTime = System.currentTimeMillis()
            if (lastMessageTime == 0L || currentTime - lastMessageTime >= 24 * 60 * 60 * 1000) {
                itemView.findViewById<TextView>(R.id.tv_chat_dateCurUsr).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.tv_chat_dateCurUsr).text = SimpleDateFormat("MMMM dd", Locale.getDefault()).format(Date(currentTime))
            } else {
                itemView.findViewById<TextView>(R.id.tv_chat_dateCurUsr).visibility = View.GONE
            }
            itemView.findViewById<TextView>(R.id.tv_chat_timestampCurUsr).text = timeFormat.format(Date(currentTime))
            lastMessageTime = currentTime
        }
    }

    inner class OthrUsrMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(chatMessage: ChatMessageResponse) {
            itemView.findViewById<TextView>(R.id.tv_chat_messageOthrUsr).text = chatMessage.message
            val currentTime = System.currentTimeMillis()
            if (lastMessageTime == 0L || currentTime - lastMessageTime >= 24 * 60 * 60 * 1000) {
                itemView.findViewById<TextView>(R.id.tv_chat_dateOthrUsr).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.tv_chat_dateOthrUsr).text = SimpleDateFormat("MMMM dd", Locale.getDefault()).format(Date(currentTime))
            } else {
                itemView.findViewById<TextView>(R.id.tv_chat_dateOthrUsr).visibility = View.GONE
            }
            itemView.findViewById<TextView>(R.id.tv_chat_timestampOthrUsr).text = timeFormat.format(Date(currentTime))
            lastMessageTime = currentTime
        }
    }

    companion object {
        private const val VIEW_TYPE_ME = 0
        private const val VIEW_TYPE_OTHER = 1
    }
}
