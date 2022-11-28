package com.sumeet.insta_fireclone.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sumeet.insta_fireclone.R
import com.sumeet.insta_fireclone.models.Post
import java.math.BigInteger
import java.security.MessageDigest


private const val TAG = "PostAdapter"
class PostAdapter(options: FirestoreRecyclerOptions<Post>, val listener:OnItemClicked): FirestoreRecyclerAdapter<Post, PostAdapter.PostViewHolder>(
    options
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false)
        val viewHolder = PostViewHolder(view)
        viewHolder.ivLikeButton.setOnClickListener {
            listener.onLikeClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }
        viewHolder.deleteButtun.setOnClickListener {
            listener.onDeleteClicked(snapshots.getSnapshot(viewHolder.adapterPosition).id)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        val username = model.createdBy.username
        holder.tvUserName.text = model.createdBy.username
        holder.tvPostDescription.text = model.description
        holder.tvCreatedAt.text = DateUtils.getRelativeTimeSpanString(model.createdAt)
        holder.tvLikeCount.text = model.likedBy.size.toString()
        Glide.with(holder.ivUploadedImage.context).load(model.imageUrlPost).apply(
            RequestOptions().transform(
                CenterCrop(), RoundedCorners(20)
            )
        ).into(holder.ivUploadedImage)
        Glide.with(holder.ivProfileImage.context).load(getProfileImageUrl(username)).into(holder.ivProfileImage)


        // Like Functionality
        val auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(currentUserId)

        if(isLiked) {
            holder.ivLikeButton.setImageDrawable(ContextCompat.getDrawable(holder.ivLikeButton.context,R.drawable.ic_liked))
        }
        else {
            holder.ivLikeButton.setImageDrawable(ContextCompat.getDrawable(holder.ivLikeButton.context,R.drawable.ic_unliked))
        }
    }




    inner class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvPostDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        val tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
        val tvCreatedAt = itemView.findViewById<TextView>(R.id.tvCreatedAt)
        val ivUploadedImage = itemView.findViewById<ImageView>(R.id.ivImageUploaded)
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvLikeCount = itemView.findViewById<TextView>(R.id.tvLikeCount)
        val deleteButtun = itemView.findViewById<ImageView>(R.id.ivDelete)
        val ivLikeButton = itemView.findViewById<ImageView>(R.id.ivLikeButton)

    }

    private fun getProfileImageUrl(username:String):String {
        val digest = MessageDigest.getInstance("MD5")
        val hash = digest.digest(username.toByteArray())
        val bigInt = BigInteger(hash)
        val hex = bigInt.abs().toString(16)
        return "https://www.gravatar.com/avatar/$hex?d=identicon"
    }
}

interface OnItemClicked {

    fun onLikeClicked(postId:String)

    fun onDeleteClicked(postId:String)
}